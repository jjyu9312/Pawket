package com.kkw.petwalker.user.service

import com.kkw.petwalker.common.response.ResponseCode
import com.kkw.petwalker.common.service.S3Service
import com.kkw.petwalker.dog.domain.Dog
import com.kkw.petwalker.dog.domain.DogType
import com.kkw.petwalker.dog.domain.Sex
import com.kkw.petwalker.dog.domain.repository.DogRepository
import com.kkw.petwalker.user.domain.Gender
import com.kkw.petwalker.user.domain.Owner
import com.kkw.petwalker.user.domain.User
import com.kkw.petwalker.user.domain.Walker
import com.kkw.petwalker.user.domain.repository.OwnerRepository
import com.kkw.petwalker.user.domain.repository.UserRepository
import com.kkw.petwalker.user.domain.repository.WalkerRepository
import com.kkw.petwalker.user.dto.CreateOwnerDto
import com.kkw.petwalker.user.dto.CreateWalkerDto
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.Cookie
import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService (
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository,
    private val ownerRepository: OwnerRepository,
    private val walkerRepository: WalkerRepository,
    private val s3Service: S3Service,
    private val request: HttpServletRequest,
    private val response: HttpServletResponse,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun login(): String {
        TODO("Not yet implemented login")
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

    fun createOwner(req: CreateOwnerDto.Req): String {
        logger.info("Creating owner with name: ${req.name}, email: ${req.email}")

        val gender = Gender.fromString(req.gender)
            ?: throw BadRequestException(
                ResponseCode.INVALID_GENDER_TYPE.withCustomMessage("- ${req.gender}")
            )

        val dogType = DogType.fromString(req.dogInfo.type)
            ?: throw BadRequestException(
                ResponseCode.INVALID_DOG_TYPE.withCustomMessage("- ${req.dogInfo.type}")
            )

        val sex = Sex.fromString(req.dogInfo.sex)
            ?: throw BadRequestException(
                ResponseCode.INVALID_SEX_TYPE.withCustomMessage("- ${req.dogInfo.sex}")
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
            throw BadRequestException("Invalid email format: ${user.email}")
        }

        if (userRepository.existsById(user.id)) {
            logger.error("User ID already exists: ${user.id}")
            throw BadRequestException("User ID already exists: ${user.id}")
        }

        val owner = Owner(userId = user.id)

        if (ownerRepository.existsById(owner.userId)) {
            logger.error("Owner ID already exists: ${owner.id}")
            throw BadRequestException("Owner ID already exists: ${owner.id}")
        }

        val filePath = req.dogInfo.imageUrl.originalFilename
            ?: throw BadRequestException("Image file not found")

        val dogImage = s3Service.uploadFile(
            bucketName = "petwalker-image",
            filePath = filePath,
            key = "${user.id}/${req.dogInfo.imageUrl.originalFilename}"
        )

        val dog = Dog(
            owner = owner,
            name = req.dogInfo.name,
            type = dogType,
            imageUrl = dogImage,
            age = req.dogInfo.age,
            sex = sex,
            weight = req.dogInfo.weight,
            isNeutered = req.dogInfo.isNeutered,
        )

        if (dogRepository.existsById(dog.id)) {
            logger.error("Dog ID already exists: ${dog.id}")
            throw BadRequestException("Dog ID already exists: : ${dog.id}")
        }

        userRepository.save(user)
        ownerRepository.save(owner)
        dogRepository.save(dog)

        return user.id
    }

    fun createWalker(req: CreateWalkerDto.Req): String {
        logger.info("Creating walker with name: ${req.name}, email: ${req.email}")

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

        val walker = Walker(
            userId = user.id,
            isExperiencedWithPets = req.walkerInfo.isExperiencedWithPets,
            petCareExperience = req.walkerInfo.petCareExperience,
        )

        userRepository.save(user)
        walkerRepository.save(walker)

        return user.id
    }
}