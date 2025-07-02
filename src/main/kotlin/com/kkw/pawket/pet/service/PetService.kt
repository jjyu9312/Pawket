package com.kkw.pawket.pet.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kkw.pawket.ai.model.common.PetProfile
import com.kkw.pawket.ai.model.res.PetProfileRes
import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.pet.domain.DogType
import com.kkw.pawket.pet.domain.Pet
import com.kkw.pawket.pet.domain.PetType
import com.kkw.pawket.pet.domain.Sex
import com.kkw.pawket.pet.domain.repository.PetRepository
import com.kkw.pawket.pet.model.req.CreatePetReq
import com.kkw.pawket.user.domain.repository.UserRepository
import com.kkw.pawket.common.exception.IllegalArgumentException
import com.kkw.pawket.common.service.S3UploadService
import com.kkw.pawket.pet.model.req.UpdatePetReq
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class PetService(
    private val petRepository: PetRepository,
    private val userRepository: UserRepository,
    private val s3UploadService: S3UploadService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val objectMapper = jacksonObjectMapper()

    fun createPet(
        userId: String,
        req: CreatePetReq,
        petImages: List<MultipartFile>,
    ): String {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val dogImageUrls = s3UploadService.uploadMultipleFiles(
            petImages,  // 리스트를 직접 전달
            dirName = "${user.id}/pet-images"  // 디렉토리 경로 지정
        )

        val pet = Pet.create(
            user = user,
            name = req.name,
            type = PetType.fromString(req.type) ?: throw IllegalArgumentException(ResponseCode.INVALID_PET_TYPE),
            dogType = req.dogType?.let { DogType.valueOf(it) },
            mainImagePath = dogImageUrls.firstOrNull(), // 리스트가 비어있으면 null
            imagePaths = dogImageUrls.joinToString(","), // 모든 이미지 URL을 쉼표로 구분
            age = req.age,
            weight = req.weight,
            sex = Sex.fromString(req.sex) ?: throw IllegalArgumentException(ResponseCode.INVALID_SEX_TYPE),
            isNeutered = req.isNeutered,
            registrationNum = req.registrationNum
        )

        if (req.petDetails != null) {
            val petDetailJson = createPetDetailJson(
                petDescription = req.petDetails.petDescription,
                foodBrand = req.petDetails.foodBrand,
                foodName = req.petDetails.foodName,
                foodType = req.petDetails.foodType
            )
            pet.petDetail = petDetailJson
        }

        logger.info("Saving pet: $pet")

        petRepository.save(pet)

        return pet.id
    }

    /*
    TODO update 시에 기존 이미지 그대로 올릴 경우 유지
     */

    // Pet 상세 정보를 JSON으로 변환
    fun createPetDetailJson(
        petDescription: String,
        foodBrand: String, // ,로 연결
        foodName: String,  // ,로 연결
        foodType: String   // ,로 연결
    ): String {
        val detailMap = mapOf(
            "petDescription" to petDescription,
            "foodBrand" to foodBrand,
            "foodName" to foodName,
            "foodType" to foodType
        )
        return objectMapper.writeValueAsString(detailMap)
    }

    fun findPetProfileByUserAndPetId(userId: String, petId: String): PetProfileRes {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val pet = petRepository.findByIdAndIsDeletedFalse(petId)
            ?: throw BadRequestException(ResponseCode.PET_NOT_FOUND)

        val petProfile = PetProfile(
            id = pet.id,
            userId = user.id,
            name = pet.name,
            type = pet.type.name,
            dogType = pet.dogType?.name,
            age = pet.age,
            weight = pet.weight,
            sex = pet.sex.name,
            isNeutered = pet.isNeutered,
            petDetail = pet.petDetail,
        )

        return PetProfileRes(
            petProfile = petProfile,
            createdAt = pet.createdAt,
            updatedAt = pet.updatedAt,
        )
    }

    fun findPetProfilesByUserId(userId: String, page: Int, size: Int): List<PetProfileRes>? {
        TODO("Not yet implemented")
    }

    fun updatePet(
        userId: String,
        petId: String,
        req: UpdatePetReq,
        petImages: List<MultipartFile>?
    ): String {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val pet = petRepository.findByIdAndIsDeletedFalse(petId)
            ?: throw BadRequestException(ResponseCode.PET_NOT_FOUND)

        if (pet.user.id != user.id) {
            throw BadRequestException(ResponseCode.UNAUTHORIZED_USER)
        }

        // 이미지가 있는 경우 S3에 업로드하고 URL을 가져옴
        if (petImages != null && petImages.isNotEmpty()) {
            val dogImageUrls = s3UploadService.uploadMultipleFiles(
                petImages,
                dirName = "${user.id}/pet-images"
            )
            pet.mainImagePath = dogImageUrls.firstOrNull() // 첫 번째 이미지를 메인 이미지로 설정
            pet.imagePaths = dogImageUrls.joinToString(",") // 모든 이미지 URL을 쉼표로 구분
        }

        pet.registrationNum = req.registrationNum
        pet.age = req.age
        pet.weight = req.weight
        pet.petDetail = if (req.petDetails != null) {
            createPetDetailJson(
                petDescription = req.petDetails.petDescription,
                foodBrand = req.petDetails.foodBrand,
                foodName = req.petDetails.foodName,
                foodType = req.petDetails.foodType
            )
        } else {
            null
        }

        petRepository.save(pet)

        return pet.id
    }
}