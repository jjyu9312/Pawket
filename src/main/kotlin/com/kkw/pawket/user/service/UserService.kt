package com.kkw.pawket.user.service

import com.kkw.pawket.ads.domain.repository.AdsRepository
import com.kkw.pawket.ads.domain.repository.CompanyRepository
import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.exception.ServerException
import com.kkw.pawket.common.exception.UnAuthorizedException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.common.service.JwtTokenProvider
import com.kkw.pawket.common.service.OAuthProviderEndpoints
import com.kkw.pawket.common.service.OAuthProviderProperties
import com.kkw.pawket.common.service.S3Service
import com.kkw.pawket.feed.repository.FeedRepository
import com.kkw.pawket.partner.domain.repository.PartnerRepository
import com.kkw.pawket.partner.domain.repository.PartnerVisitHistoryRepository
import com.kkw.pawket.pet.domain.Pet
import com.kkw.pawket.pet.domain.DogType
import com.kkw.pawket.pet.domain.PetType
import com.kkw.pawket.pet.domain.Sex
import com.kkw.pawket.pet.domain.repository.PetRepository
import com.kkw.pawket.pet.service.PetService
import com.kkw.pawket.reservation.domain.repository.ReservationRepository
import com.kkw.pawket.reward.domain.repository.RewardHistoryRepository
import com.kkw.pawket.terms.domain.repository.UserTermsMappingRepository
import com.kkw.pawket.user.domain.Gender
import com.kkw.pawket.user.domain.User
import com.kkw.pawket.user.domain.repository.UserOAuthRepository
import com.kkw.pawket.user.domain.repository.UserRepository
import com.kkw.pawket.user.model.req.CreateUserReq
import com.kkw.pawket.user.model.res.CreateUserRes
import com.kkw.pawket.user.model.res.LoginUserRes
import com.kkw.pawket.user.domain.OAuthProvider
import com.kkw.pawket.user.domain.UserOAuth
import com.kkw.pawket.walkRecord.domain.repository.WalkRecordRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.Cookie
import jakarta.transaction.Transactional
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException.Unauthorized
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
class UserService(
    private val oauthProviderProperties: OAuthProviderProperties,
    private val oauthProviderEndpoints: OAuthProviderEndpoints,
    private val restTemplate: RestTemplate,
    private val jwtTokenProvider: JwtTokenProvider,
    private val request: HttpServletRequest,
    private val response: HttpServletResponse,
    private val userRepository: UserRepository,
    private val userOAuthRepository: UserOAuthRepository,
    private val petRepository: PetRepository,
    private val partnerRepository: PartnerRepository,
    private val companyRepository: CompanyRepository,
    private val adsRepository: AdsRepository,
    private val reservationRepository: ReservationRepository,
    private val walkRecordRepository: WalkRecordRepository,
    private val feedRepository: FeedRepository,
    private val userTermsMappingRepository: UserTermsMappingRepository,
    private val partnerVisitHistoryRepository: PartnerVisitHistoryRepository,
    private val rewardHistoryRepository: RewardHistoryRepository,
    private val s3Service: S3Service,
    private val petService: PetService,
) {
    @Value("\${app.backend-url}")
    private lateinit var backendUrl: String

    @Value("\${cloud.aws.s3.bucket.name}")
    private lateinit var bucketName: String

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getOAuthRedirectUrl(provider: String): String {
        val providerInfo = providerInfo(provider)

        val providerEndpoint = providerEndpoints(provider)

        return "${providerEndpoint.authorizationUri}?client_id=${providerInfo.clientId}" +
                "&response_type = code" +
                "&scope = ${providerInfo.scope.joinToString(",")}" +
                "&redirect_uri = ${backendUrl}/oauth2/callback/$provider"
    }

    private fun getAccessToken(provider: String, code: String): OAuthTokenResponse {
        val providerInfo = providerInfo(provider)

        val providerEndpoint = providerEndpoints(provider)

        val requestBody = mapOf(
            "client_id" to providerInfo.clientId,
            "client_secret" to providerInfo.clientSecret,
            "code" to code,
            "grant_type" to "authorization_code",
            "redirect_uri" to "${backendUrl}/oauth2/callback/$provider"
        )

        val response = restTemplate.postForEntity(
            providerEndpoint.tokenUri,
            requestBody,
            OAuthTokenResponse::class.java
        )
        return response.body ?: throw UnAuthorizedException(ResponseCode.INVALID_TOKEN)
    }

    private fun getUserInfo(provider: String, accessToken: String): OAuthUser {
        val providerEndpoint = providerEndpoints(provider)

        val headers = HttpHeaders().apply {
            add("Authorization", "Bearer $accessToken")
        }

        val entity = HttpEntity(null, headers)
        val response = restTemplate.exchange(
            providerEndpoint.userInfoUri,
            HttpMethod.GET,
            entity,
            Map::class.java
        )
        val body = response.body ?: throw UnAuthorizedException(ResponseCode.OAUTH_USERINFO_INVALID)

        val email = body["email"] as? String ?: "unknown@${provider}.com"
        val providerUserId = (body["sub"] ?: body["id"])?.toString()
            ?: throw RuntimeException("OAuth 유저 식별자가 없습니다")

        return OAuthUser(
            email = email,
            provider = provider,
            providerUserId = providerUserId,
        )
    }

    // UserService에 새로운 메서드 추가
    fun findOrCreateOAuthUser(email: String, provider: OAuthProvider, providerUserId: String): User {
        // 1. 기존 OAuth 정보 확인
        val existingOAuth = userOAuthRepository.findByProviderAndProviderUserId(
            provider, providerUserId
        )

        return if (existingOAuth != null) {
            existingOAuth.user
        } else {
            // 1. 유저 생성 (소셜 로그인만으로 기본 생성)
            val newUser = User.create(email)
            userRepository.save(newUser)

            // 2. OAuth 연동 저장
            val newOAuth = UserOAuth(
                user = newUser,
                provider = provider,
                providerUserId = providerUserId,
            )
            userOAuthRepository.save(newOAuth)

            newUser
        }
    }

    fun logout(): String {
        logger.info("Logging out user with session ID: ${request.session.id}")

        // 세션 무효화
        request.session.invalidate()

        // 쿠키 삭제 (JSESSIONID)
        val cookie = Cookie("JSESSIONID", null)
        cookie.path = "/"
        cookie.maxAge = 0
        response.addCookie(cookie)

        logger.info("Logout time: ${LocalDateTime.now()}")

        // 추가적인 로그아웃 로직 (예: 외부 SNS 로그아웃 처리) 필요 시 구현
        return "로그아웃 성공"
    }

    @Transactional(rollbackOn = [Exception::class])
    fun createUser(userId: String, req: CreateUserReq): CreateUserRes {
        logger.info("Creating user with name: ${req.name}, email: ${req.email}")

        val gender = Gender.fromString(req.gender)
            ?: throw BadRequestException(
                ResponseCode.INVALID_GENDER_TYPE,
                "- ${req.gender}"
            )

        val user = userRepository.findByIdOrNull(userId)
            ?: throw BadRequestException(
                ResponseCode.USER_NOT_FOUND,
                "- $userId"
            )

        /*
        TODO S3 이미지 업로드
         */
        val imageUrl = null

        user.update(
            name = req.name,
            birth = req.birth,
            gender = gender,
            addressBasic = req.addressInfo.basic,
            addressLat = req.addressInfo.lat,
            addressLng = req.addressInfo.lng,
            addressDetail = req.addressInfo.detail,
        )

        if (!user.isValidEmail()) {
            logger.error("Invalid email format: ${user.email}")
            throw BadRequestException(
                ResponseCode.INVALID_EMAIL_FORMAT,
                " - ${user.email}"
            )
        }

        if (userRepository.existsById(user.id)) {
            logger.error("User already exists: ${user.id}")
            throw BadRequestException(
                ResponseCode.USER_CREATION_FAILED,
                "이미 존재하는 user - ${user.id}"
            )
        }

        var dogImage = ""

        req.petInfo?.imageUrls?.forEach {
            val filePath = it.originalFilename
                ?: throw BadRequestException(
                    ResponseCode.NOT_FOUND_IMAGE,
                    "- ${it.originalFilename}"
                )

            try {
                val imageUrl = s3Service.uploadFile(
                    bucketName = bucketName,
                    filePath = filePath,
                    key = "${user.id}/${it.originalFilename}"
                )

                dogImage = if (dogImage.isEmpty()) imageUrl else "$dogImage,$imageUrl"
            } catch (e: Exception) {
                logger.error("Failed to upload image to S3: $e")
            }
        }

        if (req.petInfo != null) {
            val type = PetType.fromString(req.petInfo.type)!!
            val sex = Sex.fromString(req.petInfo.sex)!!
            val pet = Pet.create(
                user = user,
                registrationNum = req.petInfo.registrationNum,
                name = req.petInfo.name,
                type = type,
                dogType = req.petInfo.dogType?.let { DogType.fromString(it) },
                mainImageUrl = if (dogImage == "") null else dogImage.split(",")[0],
                imageUrls = if (dogImage == "") null else dogImage,
                age = req.petInfo.age,
                sex = sex,
                weight = req.petInfo.weight,
                isNeutered = req.petInfo.isNeutered,
            )

            if (req.petInfo.petDetails != null) {
                val petDetailJson = petService.createPetDetailJson(
                    petDescription = req.petInfo.petDetails.petDescription,
                    foodBrand = req.petInfo.petDetails.foodBrand,
                    foodName = req.petInfo.petDetails.foodName,
                    foodType = req.petInfo.petDetails.foodType
                )
                pet.petDetail = petDetailJson
            }

            petRepository.save(pet)
        }

        userRepository.save(user)

        return CreateUserRes(userId = user.id)
    }

    private fun providerInfo(provider: String): OAuthProviderProperties.ProviderInfo {
        val providerInfo = when (provider) {
            "google" -> oauthProviderProperties.google
            "kakao" -> oauthProviderProperties.kakao
            "apple" -> oauthProviderProperties.apple
            else -> throw BadRequestException(ResponseCode.INVALID_OAUTH_PROVIDER)
        }
        logger.info("Provider info: $providerInfo")
        return providerInfo
    }

    private fun providerEndpoints(provider: String): OAuthProviderEndpoints.ProviderEndpoints {
        val providerEndpoint = when (provider) {
            "google" -> oauthProviderEndpoints.google
            "kakao" -> oauthProviderEndpoints.kakao
            "apple" -> oauthProviderEndpoints.apple
            else -> throw BadRequestException(ResponseCode.INVALID_OAUTH_PROVIDER_ENDPOINT)
        }
        logger.info("Provider endpoints: $providerEndpoint")
        return providerEndpoint
    }

    @Transactional(rollbackOn = [Exception::class])
    fun deleteUser(userId: String): Boolean {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        user.isDeleted = true

        // userOAuth 완전 삭제
        val userOAuth = userOAuthRepository.findAllByUserId(userId)
        userOAuthRepository.deleteAll(userOAuth)

        // 연관된 엔티티들도 모두 isDeleted true
        // pet, partner, ad, company, walkRecord, reservation, feed, userTermsMapping, partnerVisitHistory, rewardHistory, ...
        val pets = petRepository.findAllByUserId(userId)
        pets.forEach { it.isDeleted = true }
        userRepository.delete(user)

        val partners = partnerRepository.findAllByUserId(userId)
        partners.forEach { it.isDeleted = true }
        partnerRepository.saveAll(partners)

        val companies = companyRepository.findAllByUserId(userId)
        companies.forEach { it.isDeleted = true }
        companyRepository.saveAll(companies)

        val ads = adsRepository.findAllByCompanyIn(companies)
        ads.forEach { it.isDeleted = true }
        adsRepository.saveAll(ads)


        val walkRecords = walkRecordRepository.findAllByUserId(userId)
        walkRecords.forEach { it.isDeleted = true }
        walkRecordRepository.saveAll(walkRecords)

        val reservations = reservationRepository.findAllByUserId(userId)
        reservations.forEach { it.isDeleted = true }
        reservationRepository.saveAll(reservations)

        val feeds = feedRepository.findAllByUserId(userId)
        feeds.forEach { it.isDeleted = true }
        feedRepository.saveAll(feeds)

        val userTermsMappings = userTermsMappingRepository.findAllByUserId(userId)
        userTermsMappingRepository.deleteAll(userTermsMappings)

        val partnerVisitHistories = partnerVisitHistoryRepository.findAllByUserId(userId)
        partnerVisitHistories.forEach { it.isDeleted = true }
        partnerVisitHistoryRepository.saveAll(partnerVisitHistories)

        val rewardHistories = rewardHistoryRepository.findAllByUserId(userId)
        rewardHistories.forEach { it.isDeleted = true }
        rewardHistoryRepository.saveAll(rewardHistories)

        userRepository.save(user)

        return true
    }
}

data class OAuthTokenResponse(val accessToken: String)
data class OAuthUser(
    val email: String,
    val provider: String,
    val providerUserId: String
)