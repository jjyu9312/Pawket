package com.kkw.pawket.user

import com.kkw.pawket.ads.domain.Ads
import com.kkw.pawket.ads.domain.Company
import com.kkw.pawket.ads.domain.repository.AdsRepository
import com.kkw.pawket.ads.domain.repository.CompanyRepository
import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.common.service.JwtTokenProvider
import com.kkw.pawket.common.service.OAuthProviderEndpoints
import com.kkw.pawket.common.service.OAuthProviderProperties
import com.kkw.pawket.common.service.S3UploadService
import com.kkw.pawket.feed.domain.Feed
import com.kkw.pawket.feed.repository.FeedRepository
import com.kkw.pawket.partner.domain.Partner
import com.kkw.pawket.partner.domain.PartnerVisitHistory
import com.kkw.pawket.partner.domain.repository.PartnerRepository
import com.kkw.pawket.partner.domain.repository.PartnerVisitHistoryRepository
import com.kkw.pawket.pet.domain.Pet
import com.kkw.pawket.pet.domain.repository.PetRepository
import com.kkw.pawket.pet.service.PetService
import com.kkw.pawket.reservation.domain.Reservation
import com.kkw.pawket.reservation.domain.repository.ReservationRepository
import com.kkw.pawket.reward.domain.RewardHistory
import com.kkw.pawket.reward.domain.repository.RewardHistoryRepository
import com.kkw.pawket.terms.domain.repository.UserTermsMappingRepository
import com.kkw.pawket.user.domain.OAuthProvider
import com.kkw.pawket.user.domain.User
import com.kkw.pawket.user.domain.UserOAuth
import com.kkw.pawket.user.domain.repository.UserOAuthRepository
import com.kkw.pawket.user.domain.repository.UserRepository
import com.kkw.pawket.user.model.req.CreateUserReq
import com.kkw.pawket.user.service.UserService
import com.kkw.pawket.walkRecord.domain.WalkRecord
import com.kkw.pawket.walkRecord.domain.repository.WalkRecordRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.mockk.*
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.servlet.http.HttpSession
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

/**
 * UserService의 단위 테스트 클래스
 *
 * 모든 의존성을 Mockk로 모킹하여 UserService 자체의 로직만 테스트합니다.
 * 이 접근 방식은 외부 의존성 없이 빠르게 실행됩니다.
 */
class UserServiceUnitTest {

    // Mock 객체 생성
    private val oAuthProviderProperties = mockk<OAuthProviderProperties>(relaxed = true)
    private val oAuthProviderEndpoints = mockk<OAuthProviderEndpoints>(relaxed = true)
    private val restTemplate = mockk<RestTemplate>()
    private val jwtTokenProvider = mockk<JwtTokenProvider>()
    private val request = mockk<HttpServletRequest>()
    private val response = mockk<HttpServletResponse>()
    private val userRepository = mockk<UserRepository>()
    private val userOAuthRepository = mockk<UserOAuthRepository>()
    private val petRepository = mockk<PetRepository>()
    private val partnerRepository = mockk<PartnerRepository>()
    private val partnerVisitHistoryRepository = mockk<PartnerVisitHistoryRepository>()
    private val userTermsMappingRepository = mockk<UserTermsMappingRepository>()
    private val rewardHistoryRepository = mockk<RewardHistoryRepository>()
    private val walkRecordRepository = mockk<WalkRecordRepository>()
    private val reservationRepository = mockk<ReservationRepository>()
    private val feedRepository = mockk<FeedRepository>()
    private val adsRepository = mockk<AdsRepository>()
    private val companyRepository = mockk<CompanyRepository>()
    private val petService = mockk<PetService>()
    private val s3UploadService = mockk<S3UploadService>()

    // 테스트할 서비스 생성
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
        // 테스트마다 새로운 서비스 인스턴스와 모의 객체 생성
        userService = UserService(
            oauthProviderProperties = oAuthProviderProperties,
            oauthProviderEndpoints = oAuthProviderEndpoints,
            restTemplate = restTemplate,
            jwtTokenProvider = jwtTokenProvider,
            request = request,
            response = response,
            userRepository = userRepository,
            userOAuthRepository = userOAuthRepository,
            petRepository = petRepository,
            partnerRepository = mockk(relaxed = true),
            companyRepository = mockk(relaxed = true),
            adsRepository = mockk(relaxed = true),
            reservationRepository = mockk(relaxed = true),
            walkRecordRepository = mockk(relaxed = true),
            feedRepository = mockk(relaxed = true),
            userTermsMappingRepository = mockk(relaxed = true),
            partnerVisitHistoryRepository = mockk(relaxed = true),
            rewardHistoryRepository = mockk(relaxed = true),
            petService = petService,
            s3UploadService = s3UploadService,
        )

        // private 필드 설정
        val field = UserService::class.java.getDeclaredField("backendUrl")
        field.isAccessible = true
        field.set(userService, "https://example.com")

        val bucketField = UserService::class.java.getDeclaredField("bucketName")
        bucketField.isAccessible = true
        bucketField.set(userService, "test-bucket")
    }

    @AfterEach
    fun tearDown() {
        // 모든 mock 초기화
        clearAllMocks()
    }

    @Nested
    @DisplayName("OAuth 관련 테스트")
    inner class OAuthTests {

        @Test
        @DisplayName("OAuth 리다이렉트 URL 요청 테스트")
        fun getOAuthRedirectUrlTest() {
            // given
            val provider = "google"
            val providerInfo = OAuthProviderProperties.ProviderInfo(
                clientId = "test-client-id",
                clientSecret = "test-client-secret",
                redirectUri = "test-redirect-uri",
                scope = listOf("email", "profile")
            )
            val providerEndpoints = OAuthProviderEndpoints.ProviderEndpoints(
                authorizationUri = "https://accounts.google.com/o/oauth2/auth",
                tokenUri = "https://oauth2.googleapis.com/token",
                userInfoUri = "https://www.googleapis.com/oauth2/v3/userinfo"
            )

            every { oAuthProviderProperties.google } returns providerInfo
            every { oAuthProviderEndpoints.google } returns providerEndpoints

            // when
            val redirectUrl = userService.getOAuthRedirectUrl(provider)

            // then
            redirectUrl shouldContain "https://accounts.google.com/o/oauth2/auth"
            redirectUrl shouldContain "client_id=test-client-id"
            redirectUrl shouldContain "scope = email,profile"
            redirectUrl shouldContain "redirect_uri = https://example.com/oauth2/callback/google"
        }
    }

    @Nested
    @DisplayName("로그아웃 테스트")
    inner class LogoutTests {

        @Test
        @DisplayName("로그아웃 요청 테스트")
        fun logoutTest() {
            // given
            val session = mockk<HttpSession>()
            every { request.session } returns session
            every { session.id } returns "session-id"
            every { session.invalidate() } just runs

            justRun { response.addCookie(any<Cookie>()) }

            // when
            val result = userService.logout()

            // then
            verify { session.invalidate() }
            verify { response.addCookie(match { it.maxAge == 0 && it.name == "JSESSIONID" }) }
            result shouldBe "로그아웃 성공"
        }
    }

    @Nested
    @DisplayName("유저 생성 테스트")
    inner class CreateUserTests {

        @Test
        @DisplayName("기본 정보만 있는 유저 생성 테스트")
        fun createUserWithBasicInfoTest() {
            // given
            val userId = "user-id"
            val user = User.create("test@example.com").apply {
                val idField = User::class.java.getDeclaredField("id")
                idField.isAccessible = true
                idField.set(this, userId)
            }

            // S3 업로드 모킹 (모든 메서드에 대해)
            every {
                s3UploadService.uploadSingleFile(
                    null,
                    "${user.id}/profile"
                )
            } returns null

            every {
                s3UploadService.uploadMultipleFiles(
                    null,
                    "${user.id}/pet-images"
                )
            } returns emptyList()

            // 레포지토리 모킹
            every { userRepository.findByIdOrNull(userId) } returns user
            every { userRepository.existsById(userId) } returns false
            every { userRepository.save(any<User>()) } returns user

            val createUserReq = CreateUserReq(
                name = "Test User",
                email = "test@example.com",
                birth = LocalDate.of(1990, 1, 1),
                gender = "male",
                addressInfo = CreateUserReq.AddressInfo(
                    basic = "서울특별시 강남구",
                    lat = 37.5,
                    lng = 127.0,
                    detail = "123-456"
                ),
                imageUrl = null,
                petInfo = null,
            )

            // when
            val result = userService.createUser(userId, createUserReq)

            // then
            verify { userRepository.save(user) }
            result.userId shouldBe userId
        }

        @Test
        @DisplayName("펫 정보가 포함된 유저 생성 테스트")
        fun createUserWithPetInfoTest() {
            // given
            val userId = "user-id"
            val user = User.create("test@example.com").apply {
                val idField = User::class.java.getDeclaredField("id")
                idField.isAccessible = true
                idField.set(this, userId)
            }

            // 모든 S3 업로드 메서드에 대한 모킹
            val mockMultipartFile = mockk<MultipartFile>() {
                every { originalFilename } returns "test.jpg"
            }

            every {
                s3UploadService.uploadSingleFile(
                    mockMultipartFile,
                    "${user.id}/profile"
                )
            } returns "https://example.com/profile.jpg"

            every {
                s3UploadService.uploadMultipleFiles(
                    listOf(mockMultipartFile),
                    "${user.id}/pet-images"
                )
            } returns listOf("https://example.com/dog1.jpg", "https://example.com/dog2.jpg")

            // 레포지토리 모킹
            every { userRepository.findByIdOrNull(userId) } returns user
            every { userRepository.existsById(userId) } returns false
            every { userRepository.save(any<User>()) } returns user
            every { petRepository.save(any<Pet>()) } returns mockk()

            // PetService 목킹
            every {
                petService.createPetDetailJson(
                    petDescription = any(),
                    foodBrand = any(),
                    foodName = any(),
                    foodType = any()
                )
            } returns """{"petDescription":"귀여운 강아지","foodBrand":"로얄캐닌","foodName":"미니 어덜트","foodType":"건식"}"""

            val petDetailsReq = CreateUserReq.PetInfo.PetDetails(
                petDescription = "귀여운 강아지",
                foodBrand = "로얄캐닌",
                foodName = "미니 어덜트",
                foodType = "건식"
            )

            val petInfoReq = CreateUserReq.PetInfo(
                registrationNum = "123456789",
                name = "초코",
                type = "DOG",
                dogType = "MALTESE",
                age = 3,
                sex = "MALE",
                weight = 5,
                isNeutered = true,
                petDetails = petDetailsReq,
                imageUrls = listOf(mockMultipartFile)
            )

            val createUserReq = CreateUserReq(
                name = "Test User",
                email = "test@example.com",
                birth = LocalDate.of(1990, 1, 1),
                gender = "male",
                imageUrl = mockMultipartFile,
                addressInfo = CreateUserReq.AddressInfo(
                    basic = "서울특별시 강남구",
                    lat = 37.5,
                    lng = 127.0,
                    detail = "123-456"
                ),
                petInfo = petInfoReq
            )

            // when
            val result = userService.createUser(userId, createUserReq)

            // then
            verify { userRepository.save(user) }
            verify { petRepository.save(any<Pet>()) }
            result.userId shouldBe userId
        }

        @Test
        @DisplayName("존재하지 않는 유저 ID로 생성 시도 시 예외 발생 테스트")
        fun createUserWithNonExistentIdTest() {
            // given
            val userId = "non-existent-id"

            every { userRepository.findByIdOrNull(userId) } returns null

            val createUserReq = CreateUserReq(
                name = "Test User",
                email = "test@example.com",
                birth = LocalDate.of(1990, 1, 1),
                gender = "male",
                addressInfo = CreateUserReq.AddressInfo(
                    basic = "서울특별시 강남구",
                    lat = 37.5,
                    lng = 127.0,
                    detail = "123-456"
                ),
                petInfo = null
            )

            // when & then
            val exception = shouldThrow<BadRequestException> {
                userService.createUser(userId, createUserReq)
            }

            exception.responseCode shouldBe ResponseCode.USER_NOT_FOUND
        }
    }

    @Nested
    @DisplayName("유저 삭제 테스트")
    inner class DeleteUserTests {

        @Test
        @DisplayName("유저 삭제 성공 테스트")
        fun deleteUserSuccessTest() {
            // given
            val userId = "user-id"
            val user = mockk<User>(relaxed = true) {  // relaxed = true 추가
                every { id } returns userId
                every { email } returns "test@example.com"
                every { isDeleted } returns false  // getter 모킹 추가
                every { isDeleted = any() } just runs  // setter 모킹은 유지
            }

            every { userRepository.findByIdAndIsDeletedFalse(userId) } returns user
            every { userRepository.save(any<User>()) } returns user
            every { userRepository.delete(any<User>()) } just runs

            // 각종 레포지토리 목킹
            every { userOAuthRepository.findAllByUserId(userId) } returns emptyList()
            every { userOAuthRepository.deleteAll(any()) } just runs

            every { petRepository.findAllByUserId(userId) } returns emptyList()

            every { partnerRepository.findAllByUserId(userId) } returns emptyList()
            every { partnerRepository.saveAll(any<List<Partner>>()) } returns emptyList()

            every { companyRepository.findAllByUserId(userId) } returns emptyList()
            every { companyRepository.saveAll(any<List<Company>>()) } returns emptyList()

            every { adsRepository.findAllByCompanyIn(any()) } returns emptyList()
            every { adsRepository.saveAll(any<List<Ads>>()) } returns emptyList()

            every { walkRecordRepository.findAllByUserId(userId) } returns emptyList()
            every { walkRecordRepository.saveAll(any<List<WalkRecord>>()) } returns emptyList()

            every { reservationRepository.findAllByUserId(userId) } returns emptyList()
            every { reservationRepository.saveAll(any<List<Reservation>>()) } returns emptyList()

            every { feedRepository.findAllByUserIdAndIsDeletedFalse(userId) } returns emptyList()
            every { feedRepository.saveAll(any<List<Feed>>()) } returns emptyList()

            every { userTermsMappingRepository.findAllByUserId(userId) } returns emptyList()
            every { userTermsMappingRepository.deleteAll(any()) } just runs

            every { partnerVisitHistoryRepository.findAllByUserId(userId) } returns emptyList()
            every { partnerVisitHistoryRepository.saveAll(any<List<PartnerVisitHistory>>()) } returns emptyList()

            every { rewardHistoryRepository.findAllByUserId(userId) } returns emptyList()
            every { rewardHistoryRepository.saveAll(any<List<RewardHistory>>()) } returns emptyList()

            // when
            val result = userService.deleteUser(userId)

            // then
            verify { userRepository.save(user) }
            verify { userRepository.delete(user) }
            verify { user.isDeleted = true } // 속성이 설정되었는지 검증
            result shouldBe true
        }

        @Test
        @DisplayName("존재하지 않는 유저 삭제 요청 테스트")
        fun deleteNonExistentUserTest() {
            // given
            val userId = "non-existent-user-id"

            every { userRepository.findByIdAndIsDeletedFalse(userId) } returns null

            // when & then
            val exception = shouldThrow<BadRequestException> {
                userService.deleteUser(userId)
            }

            exception.responseCode shouldBe ResponseCode.USER_NOT_FOUND
        }
    }

    @Nested
    @DisplayName("OAuth 사용자 관리 테스트")
    inner class OAuthUserManagementTests {

        @Test
        @DisplayName("기존 OAuth 연동 정보가 있는 경우 테스트")
        fun findExistingOAuthUserTest() {
            // given
            val email = "test@example.com"
            val provider = OAuthProvider.GOOGLE
            val providerUserId = "12345"

            val userId = "user-id"
            val user = User.create(email).apply {
                // 리플렉션이나 내부 필드 설정 메서드를 사용해 id 설정
                val idField = User::class.java.getDeclaredField("id")
                idField.isAccessible = true
                idField.set(this, userId)
            }

            val userOAuth = UserOAuth.create(
                user = user,
                email = email,
                provider = provider,
                providerUserId = providerUserId,
            )

            every {
                userOAuthRepository.findByProviderAndProviderUserId(
                    provider,
                    providerUserId
                )
            } returns userOAuth

            // when
            val result = userService.findOrCreateOAuthUser(email, provider, providerUserId)

            // then
            result shouldBe user
            verify(exactly = 0) { userRepository.save(any<User>()) }
            verify(exactly = 0) { userOAuthRepository.save(any<UserOAuth>()) }
        }

        @Test
        @DisplayName("새로운 OAuth 연동이 필요한 경우 테스트")
        fun createNewOAuthUserTest() {
            // given
            val email = "test@example.com"
            val provider = OAuthProvider.GOOGLE
            val providerUserId = "12345"
            val userId = "user-id"
            val user = User.create(email).apply {
                // 리플렉션을 통해 id 설정
                val idField = User::class.java.getDeclaredField("id")
                idField.isAccessible = true
                idField.set(this, userId)
            }

            every {
                userOAuthRepository.findByProviderAndProviderUserId(
                    provider,
                    providerUserId
                )
            } returns null

            every { userRepository.save(any<User>()) } answers {
                // 첫 번째 인자(저장되는 User)의 id를 원하는 값으로 설정
                firstArg<User>().apply {
                    val idField = User::class.java.getDeclaredField("id")
                    idField.isAccessible = true
                    idField.set(this, userId)
                }
            }

            every { userOAuthRepository.save(any<UserOAuth>()) } returns UserOAuth(
                user = user,
                email = email,
                provider = provider,
                providerUserId = providerUserId
            )

            // when
            val result = userService.findOrCreateOAuthUser(email, provider, providerUserId)

            // then
            result.id shouldBe userId
            verify { userRepository.save(any<User>()) }
            verify { userOAuthRepository.save(any<UserOAuth>()) }
        }
    }
}