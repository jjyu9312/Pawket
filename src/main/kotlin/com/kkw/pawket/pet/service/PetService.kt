package com.kkw.pawket.pet.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
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
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PetService(
    private val petRepository: PetRepository,
    private val userRepository: UserRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    private val objectMapper = jacksonObjectMapper()

    fun createPet(userId: String, req: CreatePetReq): String {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val pet = Pet.create(
            user = user,
            name = req.name,
            type = PetType.fromString(req.type) ?: throw IllegalArgumentException(ResponseCode.INVALID_PET_TYPE),
            dogType = req.dogType?.let { DogType.valueOf(it) },
            mainImageUrl = null,
            imageUrls = null,
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
}