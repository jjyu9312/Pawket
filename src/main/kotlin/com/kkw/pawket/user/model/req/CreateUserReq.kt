package com.kkw.pawket.user.model.req

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

@Schema(description = "Request object for creating a new user", name = "CreateUserReq")
data class CreateUserReq (
    @Schema(description = "User's email address", example = "user@example.com")
    val email: String,

    @Schema(description = "User's full name", example = "Kyle Kim")
    val name: String,

    @Schema(description = "User's date of birth", example = "1993-12-11")
    val birth: LocalDate,

    @Schema(description = "User's gender (male/female/other)", example = "male")
    val gender: String,

    @Schema(description = "Profile image file of the user")
    val imageUrl: MultipartFile? = null,

    @Schema(description = "User's address information")
    val addressInfo: AddressInfo,

    @Schema(description = "Information about the user's pet")
    val petInfo: PetInfo? = null,
) {
    @Schema(description = "Address information of the user", name = "AddressInfo")
    data class AddressInfo (
        @Schema(description = "Basic address", example = "123 Main St, Seoul")
        val basic: String,

        @Schema(description = "Detailed address (optional)", example = "Apt 101")
        val detail: String? = null,

        @Schema(description = "Latitude of the address", example = "37.5665")
        val lat: Double,

        @Schema(description = "Longitude of the address", example = "126.9780")
        val lng: Double
    )

    @Schema(description = "Information about the user's pet", name = "PetInfo")
    data class PetInfo (
        @Schema(description = "Pet registration number", example = "A123456")
        val registrationNum: String? = null,

        @Schema(description = "Name of the pet", example = "Kong-i")
        val name: String,

        @Schema(description = "Type of the pet (e.g., dog, cat)", example = "dog")
        val type: String,

        @Schema(description = "Breed of the dog", example = "Pomeranian")
        val dogType: String? = null,

        @Schema(description = "List of image files of the pet", example = "dog1.jpg")
        val imageUrls: List<MultipartFile>,

        @Schema(description = "Age of the pet", example = "3")
        val age: Int,

        @Schema(description = "Sex of the pet (male/female)", example = "male")
        val sex: String,

        @Schema(description = "Weight of the pet (kg)", example = "5")
        val weight: Int,

        @Schema(description = "Whether the pet is neutered", example = "true")
        val isNeutered: Boolean,

        @Schema(description = "Additional details about the pet")
        val petDetails: PetDetails? = null,
    ) {
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
    }
}