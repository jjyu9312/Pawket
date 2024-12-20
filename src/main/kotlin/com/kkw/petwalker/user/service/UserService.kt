package com.kkw.petwalker.user.service

import com.kkw.petwalker.common.response.ApiResponse
import com.kkw.petwalker.common.response.ApiResponseFactory
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
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository,
    private val ownerRepository: OwnerRepository,
    private val walkerRepository: WalkerRepository,
    private val s3Service: S3Service,
) {

    fun login(): String {
        TODO("Not yet implemented")
    }

    fun logout(): Any {
        TODO("Not yet implemented")
    }

    fun createOwner(req: CreateOwnerDto.Req): String {
        val user = User(
            name = req.name,
            birth = req.birth,
            email = req.email,
            gender = Gender.fromString(req.gender)!!,
            addressBasic = req.addressInfo.basic,
            addressLat = req.addressInfo.lat,
            addressLng = req.addressInfo.lng,
            addressDetail = req.addressInfo.detail,
        )

        if (!user.isValidEmail()) {
            throw BadRequestException("Invalid email format: ${user.email}")
        }

        if (userRepository.existsById(user.id)) {
            throw BadRequestException("User ID already exists: ${user.id}")
        }

        val owner = Owner(userId = user.id)

        if (ownerRepository.existsById(owner.userId)) {
            throw BadRequestException("Owner ID already exists: ${owner.id}")
        }

        val dogImage = s3Service.uploadFile(
            bucketName = "petwalker-image",
            filePath = req.dogInfo.imageUrl.originalFilename!!,
            key = "${user.id}/${req.dogInfo.imageUrl.originalFilename}"
        )

        val dog = Dog(
            owner = owner,
            name = req.dogInfo.name,
            type = DogType.fromString(req.dogInfo.type)!!,
            imageUrl = dogImage,
            age = req.dogInfo.age,
            sex = Sex.fromString(req.dogInfo.sex)!!,
            weight = req.dogInfo.weight,
            isNeutered = req.dogInfo.isNeutered,
        )

        if (dogRepository.existsById(dog.id)) {
            throw BadRequestException("Dog ID already exists: : ${dog.id}")
        }

        userRepository.save(user)
        ownerRepository.save(owner)
        dogRepository.save(dog)

        return user.id
    }

    fun createWalker(req: CreateWalkerDto.Req): String {
        val user = User(
            name = req.name,
            birth = req.birth,
            email = req.email,
            gender = Gender.fromString(req.gender)!!,
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