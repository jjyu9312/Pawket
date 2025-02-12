package com.kkw.petwalker.user.service

import com.kkw.petwalker.common.response.ResponseCode
import com.kkw.petwalker.common.service.S3Service
import com.kkw.petwalker.common.util.JwtUtil
import com.kkw.petwalker.pet.domain.Pet
import com.kkw.petwalker.pet.domain.DogType
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
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
class UserService (
    private val userRepository: UserRepository,
    private val petRepository: PetRepository,
    private val restTemplate: RestTemplate,
    private val s3Service: S3Service,
    private val request: HttpServletRequest,
    private val response: HttpServletResponse,
    private val jwtUtil: JwtUtil,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun getOAuthRedirectUrl(provider: String): String {
        return when (provider) {
            "google" -> "https://accounts.google.com/o/oauth2/auth?client_id=YOUR_GOOGLE_CLIENT_ID&response_type=code&scope=email%20profile&redirect_uri=YOUR_BACKEND_URL/user/oauth/callback/google"
            "kakao" -> "https://kauth.kakao.com/oauth/authorize?client_id=YOUR_KAKAO_CLIENT_ID&response_type=code&redirect_uri=YOUR_BACKEND_URL/user/oauth/callback/kakao"
            "apple" -> "https://appleid.apple.com/auth/authorize?client_id=YOUR_APPLE_CLIENT_ID&response_type=code&redirect_uri=YOUR_BACKEND_URL/user/oauth/callback/apple"
            else -> throw IllegalArgumentException("Unsupported provider: $provider")
        }
    }

    fun handleOAuthCallback(provider: String, code: String): LoginUserDto {
        val tokenResponse = getAccessToken(provider, code)
        val userInfo = getUserInfo(provider, tokenResponse.accessToken)
        val jwtToken = jwtUtil.generateToken(userInfo.email)

        return LoginUserDto(
            email = userInfo.email,
            provider = provider,
            token = jwtToken
        )
    }

    private fun getAccessToken(provider: String, code: String): OAuthTokenResponse {
        val url = when (provider) {
            "google" -> "https://oauth2.googleapis.com/token"
            "kakao" -> "https://kauth.kakao.com/oauth/token"
            "apple" -> "https://appleid.apple.com/auth/token"
            else -> throw IllegalArgumentException("Unsupported provider: $provider")
        }

        val requestBody = mapOf(
            "client_id" to "YOUR_${provider.uppercase()}_CLIENT_ID",
            "client_secret" to "YOUR_${provider.uppercase()}_CLIENT_SECRET",
            "code" to code,
            "grant_type" to "authorization_code",
            "redirect_uri" to "YOUR_BACKEND_URL/user/oauth/callback/$provider"
        )

        val response = restTemplate.postForEntity(url, requestBody, OAuthTokenResponse::class.java)
        return response.body ?: throw RuntimeException("Failed to retrieve access token")
    }

    private fun getUserInfo(provider: String, accessToken: String): OAuthUser {
        val url = when (provider) {
            "google" -> "https://www.googleapis.com/oauth2/v2/userinfo"
            "kakao" -> "https://kapi.kakao.com/v2/user/me"
            "apple" -> "https://appleid.apple.com/auth/userinfo"
            else -> throw IllegalArgumentException("Unsupported provider: $provider")
        }

        val headers = HttpHeaders().apply {
            add("Authorization", "Bearer $accessToken")
        }

        val entity = HttpEntity(null, headers)
        val response = restTemplate.exchange(url, HttpMethod.GET, entity, Map::class.java)
        val body = response.body ?: throw RuntimeException("Failed to retrieve user info")

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
        logger.info("Creating owner with name: ${req.name}, email: ${req.email}")

        val gender = Gender.fromString(req.gender)
            ?: throw BadRequestException(
                ResponseCode.INVALID_GENDER_TYPE.withCustomMessage("- ${req.gender}")
            )

        val dogType = DogType.fromString(req.petInfo.type)
            ?: throw BadRequestException(
                ResponseCode.INVALID_DOG_TYPE.withCustomMessage("- ${req.petInfo.type}")
            )

        val sex = Sex.fromString(req.petInfo.sex)
            ?: throw BadRequestException(
                ResponseCode.INVALID_SEX_TYPE.withCustomMessage("- ${req.petInfo.sex}")
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

        req.petInfo.imageUrls.forEach {
            val filePath = it.originalFilename
                ?: throw BadRequestException(
                    ResponseCode.NOT_FOUND_IMAGE.withCustomMessage("- ${it.originalFilename}")
                )

            val imageUrl = s3Service.uploadFile(
                bucketName = "petwalker-image",
                filePath = filePath,
                key = "${user.id}/${it.originalFilename}"
            )

            dogImage = if (dogImage.isEmpty()) imageUrl else "$dogImage,$imageUrl"
        }

        val pet = Pet(
            user = user,
            registrationNum = req.petInfo.registrationNum,
            name = req.petInfo.name,
            type = dogType,
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

        userRepository.save(user)
        petRepository.save(pet)

        return user.id
    }

}

data class OAuthTokenResponse(val accessToken: String)
data class OAuthUser(val email: String, val provider: String)