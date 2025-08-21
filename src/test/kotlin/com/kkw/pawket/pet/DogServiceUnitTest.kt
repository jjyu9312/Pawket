package com.kkw.pawket.dog

import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.dog.domain.repository.DogRepository
import com.kkw.pawket.dog.model.req.CreateDogReq
import com.kkw.pawket.dog.service.DogService
import com.kkw.pawket.user.domain.User
import com.kkw.pawket.user.domain.repository.UserRepository
import com.kkw.pawket.common.service.S3UploadService
import com.kkw.pawket.dog.domain.DogType
import com.kkw.pawket.dog.domain.Dog
import com.kkw.pawket.dog.domain.Sex
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.multipart.MultipartFile

class DogServiceUnitTest {
    private val dogRepository = mockk<DogRepository>()
    private val userRepository = mockk<UserRepository>()
    private val s3UploadService = mockk<S3UploadService>()

    private lateinit var dogService: DogService

    @BeforeEach
    fun setUp() {
        dogService = DogService(dogRepository, userRepository, s3UploadService)
        clearAllMocks()
    }

    @Test
    fun `유저가 존재하지 않을 때 예외 발생`() {
        // given
        val userId = "user1"
        val req = mockk<CreateDogReq>()
        val images = listOf<MultipartFile>()

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns null

        // when & then
        val exception = shouldThrow<BadRequestException> {
            dogService.createDog(userId, req, images)
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

        val req = mockk<CreateDogReq> {
            every { name } returns "강아지"
            every { type } returns "invalid"
            every { age } returns 3
            every { weight } returns 5
            every { sex } returns "MALE"
            every { isNeutered } returns false
            every { registrationNum } returns "123"
            every { dogDetails } returns null
        }
        val images = listOf<MultipartFile>()

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns user

        // when & then
        val exception = shouldThrow<com.kkw.pawket.common.exception.IllegalArgumentException> {
            dogService.createDog(userId, req, images)
        }
        exception.responseCode shouldBe ResponseCode.INVALID_DOG_TYPE
    }

    @Test
    fun `정상적으로 펫 생성`() {
        // given
        val userId = "user1"
        val user = mockk<User> {
            every { id } returns userId
        }
        val req = mockk<CreateDogReq> {
            every { name } returns "강아지"
            every { type } returns "MALTESE"
            every { age } returns 3
            every { weight } returns 5
            every { sex } returns "MALE"
            every { isNeutered } returns false
            every { registrationNum } returns "123"
            every { dogDetails } returns null
        }
        val images = listOf<MultipartFile>(mockk(), mockk())
        val imageUrls = listOf("url1", "url2")
        val dog = mockk<Dog> {
            every { id } returns "dog1"
        }

        every { userRepository.findByIdAndIsDeletedFalse(userId) } returns user
        every { s3UploadService.uploadMultipleFiles(images, "$userId/dog-images") } returns imageUrls
        mockkObject(Dog.Companion)
        every {
            Dog.create(
                user = user,
                name = "강아지",
                type = DogType.MALTESE,
                mainImagePath = "url1",
                imagePaths = "url1,url2",
                age = 3,
                weight = 5,
                sex = Sex.MALE,
                isNeutered = false,
                registrationNum = "123"
            )
        } returns dog
        every { dogRepository.save(dog) } returns dog

        // when
        val result = dogService.createDog(userId, req, images)

        // then
        result shouldBe "dog1"
        verify { dogRepository.save(dog) }
    }
}