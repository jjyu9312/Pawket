package com.kkw.pawket.pet

import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.pet.domain.*
import com.kkw.pawket.pet.domain.repository.PetRepository
import com.kkw.pawket.pet.model.req.CreatePetReq
import com.kkw.pawket.pet.service.PetService
import com.kkw.pawket.user.domain.User
import com.kkw.pawket.user.domain.repository.UserRepository
import com.kkw.pawket.common.service.S3UploadService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.multipart.MultipartFile

class PetServiceUnitTest {
    private val petRepository = mockk<PetRepository>()
    private val userRepository = mockk<UserRepository>()
    private val s3UploadService = mockk<S3UploadService>()

    private lateinit var petService: PetService

    @BeforeEach
    fun setUp() {
        petService = PetService(petRepository, userRepository, s3UploadService)
        clearAllMocks()
    }

    @Test
    fun `유저가 존재하지 않을 때 예외 발생`() {
        // given
        val userId = "user1"
        val req = mockk<CreatePetReq>()
        val images = listOf<MultipartFile>()

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns null

        // when & then
        val exception = shouldThrow<BadRequestException> {
            petService.createPet(userId, req, images)
        }
        exception.responseCode shouldBe ResponseCode.USER_NOT_FOUND
    }

    @Test
    fun `펫 타입이 잘못된 경우 예외 발생`() {
        // given
        val userId = "user1"
        val user = mockk<User>()
        every { user.id } returns userId
        every { s3UploadService.uploadMultipleFiles(any(), any()) } returns emptyList()

        val req = mockk<CreatePetReq> {
            every { name } returns "강아지"
            every { type } returns "invalid"
            every { dogType } returns null
            every { age } returns 3
            every { weight } returns 5
            every { sex } returns "MALE"
            every { isNeutered } returns false
            every { registrationNum } returns "123"
            every { petDetails } returns null
        }
        val images = listOf<MultipartFile>()

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns user

        // when & then
        val exception = shouldThrow<com.kkw.pawket.common.exception.IllegalArgumentException> {
            petService.createPet(userId, req, images)
        }
        exception.responseCode shouldBe ResponseCode.INVALID_PET_TYPE
    }

    @Test
    fun `정상적으로 펫 생성`() {
        // given
        val userId = "user1"
        val user = mockk<User> {
            every { id } returns userId
        }
        val req = mockk<CreatePetReq> {
            every { name } returns "강아지"
            every { type } returns "DOG"
            every { dogType } returns "MALTESE"
            every { age } returns 3
            every { weight } returns 5
            every { sex } returns "MALE"
            every { isNeutered } returns false
            every { registrationNum } returns "123"
            every { petDetails } returns null
        }
        val images = listOf<MultipartFile>(mockk(), mockk())
        val imageUrls = listOf("url1", "url2")
        val pet = mockk<Pet> {
            every { id } returns "pet1"
        }

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns user
        every { s3UploadService.uploadMultipleFiles(images, "$userId/pet-images") } returns imageUrls
        mockkObject(Pet.Companion)
        every {
            Pet.create(
                user = user,
                name = "강아지",
                type = PetType.DOG,
                dogType = DogType.MALTESE,
                mainImagePath = "url1",
                imagePaths = "url1,url2",
                age = 3,
                weight = 5,
                sex = Sex.MALE,
                isNeutered = false,
                registrationNum = "123"
            )
        } returns pet
        every { petRepository.save(pet) } returns pet

        // when
        val result = petService.createPet(userId, req, images)

        // then
        result shouldBe "pet1"
        verify { petRepository.save(pet) }
    }
}