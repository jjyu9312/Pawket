package com.kkw.petwalker.user.service

import com.kkw.petwalker.common.response.ResponseCode
import com.kkw.petwalker.common.service.JwtTokenProvider
import com.kkw.petwalker.common.service.OAuthProviderEndpoints
import com.kkw.petwalker.common.service.OAuthProviderProperties
import com.kkw.petwalker.common.service.S3Service
import com.kkw.petwalker.pet.domain.Pet
import com.kkw.petwalker.pet.domain.DogType
import com.kkw.petwalker.pet.domain.PetType
import com.kkw.petwalker.pet.domain.Sex
import com.kkw.petwalker.pet.domain.repository.PetRepository
import com.kkw.petwalker.user.domain.Gender
import com.kkw.petwalker.user.domain.User
import com.kkw.petwalker.user.domain.repository.UserRepository
import com.kkw.petwalker.user.dto.CreateUserDto
import com.kkw.petwalker.user.dto.LoginUserDto
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.Cookie
import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
class UserService (
    private val oauthProviderProperties: OAuthProviderProperties,
    private val oauthProviderEndpoints: OAuthProviderEndpoints,
    private val restTemplate: RestTemplate,
    private val jwtTokenProvider: JwtTokenProvider,
    private val request: HttpServletRequest,
    private val response: HttpServletResponse,
    private val userRepository: UserRepository,
    private val petRepository: PetRepository,
    private val s3Service: S3Service,
) {
    @Value("\${backend.url}")
    private lateinit var backendUrl: String

    @Value("\${aws.s3.bucket.name}")
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

    fun handleOAuthCallback(provider: String, code: String): LoginUserDto {
        val tokenResponse = getAccessToken(provider, code)
        val userInfo = getUserInfo(provider, tokenResponse.accessToken)
        val jwtToken = jwtTokenProvider.createToken(userInfo.email)
        logger.info("User info: $userInfo, JWT token: $jwtToken")

        return LoginUserDto(
            email = userInfo.email,
            provider = provider,
            token = jwtToken
        )
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

        val response = restTemplate.postForEntity(providerEndpoint.tokenUri, requestBody, OAuthTokenResponse::class.java)
        return response.body ?: throw RuntimeException(
            ResponseCode.OAUTH_TOKEN_INVALID.defaultMessage
        )
    }

    private fun getUserInfo(provider: String, accessToken: String): OAuthUser {
        val providerEndpoint = providerEndpoints(provider)

        val headers = HttpHeaders().apply {
            add("Authorization", "Bearer $accessToken")
        }

        val entity = HttpEntity(null, headers)
        val response = restTemplate.exchange(providerEndpoint.userInfoUri, HttpMethod.GET, entity, Map::class.java)
        val body = response.body ?: throw RuntimeException(
            ResponseCode.OAUTH_USERINFO_INVALID.defaultMessage
        )

        return OAuthUser(
            email = body["email"] as? String ?: "unknown@${provider}.com",
            provider = provider
        )
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

    fun createUser(req: CreateUserDto.Req): String {
        logger.info("Creating user with name: ${req.name}, email: ${req.email}")

        val gender = Gender.fromString(req.gender)
            ?: throw BadRequestException(
                ResponseCode.INVALID_GENDER_TYPE.withCustomMessage("- ${req.gender}")
            )

        val user = User(
            name = req.name,
            birth = req.birth,
            email = req.email,
            gender = gender,
            addressBasic = req.addressInfo.basic,
            addressLat = req.addressInfo.lat,
            addressLng = req.addressInfo.lng,
            addressDetail = req.addressInfo.detail,
        )

        if (!user.isValidEmail()) {
            logger.error("Invalid email format: ${user.email}")
            throw BadRequestException(
                ResponseCode.INVALID_EMAIL_FORMAT.withCustomMessage(" - ${user.email}")
            )
        }

        if (userRepository.existsById(user.id)) {
            logger.error("User already exists: ${user.id}")
            throw BadRequestException(
                ResponseCode.USER_CREATION_FAILED.withCustomMessage("이미 존재하는 user - ${user.id}")
            )
        }

        var dogImage = ""

        req.petInfo?.imageUrls?.forEach {
            val filePath = it.originalFilename
                ?: throw BadRequestException(
                    ResponseCode.NOT_FOUND_IMAGE.withCustomMessage("- ${it.originalFilename}")
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
            val pet = Pet(
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
                dogDescription = req.petInfo.dogDescription,
                foodBrand = req.petInfo.foodBrand,
                foodName = req.petInfo.foodName,
                foodType = req.petInfo.foodType,
            )

            if (petRepository.existsById(pet.id)) {
                logger.error("ID already exists: ${pet.id}")
                throw BadRequestException(
                    ResponseCode.DOG_CREATION_FAILED.withCustomMessage("이미 존재하는 dog - ${pet.id}")
                )
            }
            petRepository.save(pet)
        }

        userRepository.save(user)

        return user.id
    }

    private fun providerInfo(provider: String): OAuthProviderProperties.ProviderInfo {
        val providerInfo = when (provider) {
            "google" -> oauthProviderProperties.google
            "kakao" -> oauthProviderProperties.kakao
            "apple" -> oauthProviderProperties.apple
            else -> throw IllegalArgumentException("OAuth2 Provider 정보를 찾을 수 없습니다: $provider")
        }
        logger.info("Provider info: $providerInfo")
        return providerInfo
    }

    private fun providerEndpoints(provider: String): OAuthProviderEndpoints.ProviderEndpoints {
        val providerEndpoint = when (provider) {
            "google" -> oauthProviderEndpoints.google
            "kakao" -> oauthProviderEndpoints.kakao
            "apple" -> oauthProviderEndpoints.apple
            else -> throw IllegalArgumentException("OAuth2 Provider 설정을 찾을 수 없습니다: $provider")
        }
        logger.info("Provider endpoints: $providerEndpoint")
        return providerEndpoint
    }
}

data class OAuthTokenResponse(val accessToken: String)
data class OAuthUser(val email: String, val provider: String)