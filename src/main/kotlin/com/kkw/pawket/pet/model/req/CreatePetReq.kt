package com.kkw.pawket.pet.model.req

import com.kkw.pawket.user.model.req.CreateUserReq
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.web.multipart.MultipartFile

data class CreatePetReq(
    @Schema(description = "Registration number of the pet", example = "123456789")
    val registrationNum: String?,

    @Schema(description = "Name of the pet", example = "콩이")
    val name: String,

    @Schema(description = "Type of the pet", example = "강아지")
    val type: String,

    @Schema(description = "Type of the dog", example = "포메라니안")
    val dogType: String?,

    @Schema(description = "Age of the pet", example = "3")
    val age: Int,

    @Schema(description = "Weight of the pet ", example = "5")
    val weight: Int,

    @Schema(description = "Sex of the pet (male/female)", example = "male")
    val sex: String,

    @Schema(description = "neutered status of the pet", example = "true")
    val isNeutered: Boolean,

    @Schema(
        description = "pet details including description, food brand, name, and type",
        example = "{\"petDescription\": \"Friendly and playful\", \"foodBrand\": \"BrandX\", \"foodName\": \"Premium Dog Food\", \"foodType\": \"Dry\"}"
    )
    val petDetails: PetDetails? = null,
) {
    data class PetDetails(
        val petDescription: String,
        val foodBrand: String,
        val foodName: String,
        val foodType: String,
    )
}

