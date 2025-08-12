package com.kkw.pawket.dog.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kkw.pawket.ai.model.common.DogProfile
import com.kkw.pawket.ai.model.res.DogProfileRes
import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.dog.domain.DogType
import com.kkw.pawket.dog.domain.Dog
import com.kkw.pawket.dog.domain.Sex
import com.kkw.pawket.dog.model.req.CreateDogReq
import com.kkw.pawket.user.domain.repository.UserRepository
import com.kkw.pawket.common.exception.IllegalArgumentException
import com.kkw.pawket.common.service.S3UploadService
import com.kkw.pawket.dog.domain.repository.DogRepository
import com.kkw.pawket.dog.model.req.UpdateDogReq
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class DogService(
    private val dogRepository: DogRepository,
    private val userRepository: UserRepository,
    private val s3UploadService: S3UploadService,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val objectMapper = jacksonObjectMapper()

    fun createDog(
        userId: String,
        req: CreateDogReq,
        dogImages: List<MultipartFile>,
    ): String {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val dogImageUrls = s3UploadService.uploadMultipleFiles(
            dogImages,  // 리스트를 직접 전달
            dirName = "${user.id}/dog-images"  // 디렉토리 경로 지정
        )

        val dog = Dog.create(
            user = user,
            name = req.name,
            type = DogType.valueOf(req.type),
            mainImagePath = dogImageUrls.firstOrNull(), // 리스트가 비어있으면 null
            imagePaths = dogImageUrls.joinToString(","), // 모든 이미지 URL을 쉼표로 구분
            age = req.age,
            weight = req.weight,
            sex = Sex.fromString(req.sex) ?: throw IllegalArgumentException(ResponseCode.INVALID_SEX_TYPE),
            isNeutered = req.isNeutered,
            registrationNum = req.registrationNum
        )

        if (req.dogDetails != null) {
            val dogDetailJson = createdogDetailJson(
                dogDescription = req.dogDetails.dogDescription,
                foodBrand = req.dogDetails.foodBrand,
                foodName = req.dogDetails.foodName,
                foodType = req.dogDetails.foodType
            )
            dog.dogDetail = dogDetailJson
        }

        logger.info("Saving dog: $dog")

        dogRepository.save(dog)

        return dog.id
    }

    /*
    TODO update 시에 기존 이미지 그대로 올릴 경우 유지
     */

    // dog 상세 정보를 JSON으로 변환
    fun createdogDetailJson(
        dogDescription: String,
        foodBrand: String, // ,로 연결
        foodName: String,  // ,로 연결
        foodType: String   // ,로 연결
    ): String {
        val detailMap = mapOf(
            "dogDescription" to dogDescription,
            "foodBrand" to foodBrand,
            "foodName" to foodName,
            "foodType" to foodType
        )
        return objectMapper.writeValueAsString(detailMap)
    }

    fun findDogProfileByUserAndDogId(userId: String, dogId: String): DogProfileRes {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val dog = dogRepository.findByIdAndIsDeletedFalse(dogId)
            ?: throw BadRequestException(ResponseCode.DOG_NOT_FOUND)

        val dogProfile = DogProfile(
            id = dog.id,
            userId = user.id,
            name = dog.name,
            type = dog.type.name,
            age = dog.age,
            weight = dog.weight,
            sex = dog.sex.name,
            isNeutered = dog.isNeutered,
            dogDetail = dog.dogDetail,
        )

        return DogProfileRes(
            dogProfile = dogProfile,
            createdAt = dog.createdAt,
            updatedAt = dog.updatedAt,
        )
    }

    fun finddogProfilesByUserId(userId: String, page: Int, size: Int): List<DogProfileRes>? {
        TODO("Not yet implemented")
    }

    fun updateDog(
        userId: String,
        dogId: String,
        req: UpdateDogReq,
        dogImages: List<MultipartFile>?
    ) {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val dog = dogRepository.findByIdAndIsDeletedFalse(dogId)
            ?: throw BadRequestException(ResponseCode.DOG_NOT_FOUND)

        if (dog.user.id != user.id) {
            throw BadRequestException(ResponseCode.UNAUTHORIZED_USER)
        }

        // 이미지가 있는 경우 S3에 업로드하고 URL을 가져옴
        if (dogImages != null && dogImages.isNotEmpty()) {
            val dogImageUrls = s3UploadService.uploadMultipleFiles(
                dogImages,
                dirName = "${user.id}/dog-images"
            )
            dog.mainImagePath = dogImageUrls.firstOrNull() // 첫 번째 이미지를 메인 이미지로 설정
            dog.imagePaths = dogImageUrls.joinToString(",") // 모든 이미지 URL을 쉼표로 구분
        }

        dog.registrationNum = req.registrationNum
        dog.age = req.age
        dog.weight = req.weight
        dog.dogDetail = if (req.dogDetails != null) {
            createdogDetailJson(
                dogDescription = req.dogDetails.dogDescription,
                foodBrand = req.dogDetails.foodBrand,
                foodName = req.dogDetails.foodName,
                foodType = req.dogDetails.foodType
            )
        } else {
            null
        }

        dogRepository.save(dog)
    }

    fun deleteDog(userId: String, dogId: String) {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val dog = dogRepository.findByIdAndIsDeletedFalse(dogId)
            ?: throw BadRequestException(ResponseCode.DOG_NOT_FOUND)

        if (userId != dog.user.id) {
            throw BadRequestException(ResponseCode.UNAUTHORIZED_USER)
        }

        dog.isDeleted = true

        dogRepository.save(dog)
    }
}