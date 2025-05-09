package com.kkw.pawket.user.model.req

import com.kkw.pawket.pet.domain.PetType
import com.kkw.pawket.pet.domain.Sex
import com.kkw.pawket.user.domain.Gender
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.time.LocalDate

@Schema(description = "Request object for creating a new user", name = "CreateUserReq")
data class CreateUserReq (
    @field:NotBlank(message = "이메일은 필수입니다")
    @field:Email(message = "유효한 이메일 형식이 아닙니다")
    @Schema(description = "User's email address", example = "user@example.com")
    val email: String,

    @field:NotBlank(message = "이름은 필수입니다")
    @Schema(description = "User's full name", example = "Kyle Kim")
    val name: String,

    @field:NotNull(message = "생년월일은 필수입니다")
    @field:Past(message = "생년월일은 과거 날짜여야 합니다")
    @Schema(description = "User's date of birth", example = "1993-12-11")
    val birth: LocalDate,

    @field:NotBlank(message = "성별은 필수입니다")
    @Schema(description = "User's gender (MALE/FEMALE/OTHER)", example = "MALE")
    val gender: String,

    @Schema(description = "Profile image URL", example = "https://storage.pawket.com/users/profile123.jpg")
    val imageUrl: String? = null,

    @field:Valid
    @field:NotNull(message = "주소 정보는 필수입니다")
    @Schema(description = "User's address information")
    val addressInfo: AddressInfo,

    @field:Valid
    @Schema(description = "Information about the user's pet")
    val petInfo: PetInfo? = null,
)

@Schema(description = "Address information of the user", name = "AddressInfo")
data class AddressInfo (
    @field:NotBlank(message = "기본 주소는 필수입니다")
    @Schema(description = "Basic address", example = "123 Main St, Seoul")
    val basic: String,

    @Schema(description = "Detailed address (optional)", example = "Apt 101")
    val detail: String? = null,

    @field:NotNull(message = "위도는 필수입니다")
    @Schema(description = "Latitude of the address", example = "37.5665")
    val lat: Double,

    @field:NotNull(message = "경도는 필수입니다")
    @Schema(description = "Longitude of the address", example = "126.9780")
    val lng: Double
)

@Schema(description = "Information about the user's pet", name = "PetInfo")
data class PetInfo (
    @Schema(description = "Pet registration number", example = "A123456")
    val registrationNum: String? = null,

    @field:NotBlank(message = "반려동물 이름은 필수입니다")
    @Schema(description = "Name of the pet", example = "Kong-i")
    val name: String,

    @field:NotNull(message = "반려동물 종류는 필수입니다")
    @Schema(description = "Type of the pet (DOG, CAT, OTHER)", example = "DOG")
    val type: String,

    @Schema(description = "Breed of the dog", example = "Pomeranian")
    val dogType: String? = null,

    @Schema(description = "List of pet image URLs", example = "[\"https://storage.pawket.com/pets/dog1.jpg\"]")
    val imageUrls: List<String> = emptyList(),

    @field:NotNull(message = "반려동물 나이는 필수입니다")
    @field:Min(value = 0, message = "반려동물 나이는 0 이상이어야 합니다")
    @Schema(description = "Age of the pet", example = "3")
    val age: Int,

    @field:NotNull(message = "반려동물 성별은 필수입니다")
    @Schema(description = "Sex of the pet (MALE/FEMALE)", example = "MALE")
    val sex: String,

    @field:NotNull(message = "반려동물 체중은 필수입니다")
    @field:Min(value = 0, message = "반려동물 체중은 0 이상이어야 합니다")
    @Schema(description = "Weight of the pet (kg)", example = "5")
    val weight: Int,

    @field:NotNull(message = "중성화 여부는 필수입니다")
    @Schema(description = "Whether the pet is neutered", example = "true")
    val isNeutered: Boolean,

    @field:Valid
    @Schema(description = "Additional details about the pet")
    val petDetails: PetDetails? = null,
)

@Schema(description = "Additional details about the pet", name = "PetDetails")
data class PetDetails(
    @Schema(description = "Description of the pet", example = "Loves playing fetch")
    val petDescription: String,

    @Schema(description = "Brand of the pet's food", example = "Royal Canin")
    val foodBrand: String,

    @Schema(description = "Name of the pet's food", example = "Mini Adult")
    val foodName: String,

    @Schema(description = "Type of the pet's food", example = "Dry food")
    val foodType: String,
)