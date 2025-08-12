package com.kkw.pawket.dog.model.req

import io.swagger.v3.oas.annotations.media.Schema

data class CreateDogReq(
    @Schema(description = "Registration number of the pet", example = "123456789")
    val registrationNum: String?,

    @Schema(description = "Name of the pet", example = "콩이")
    val name: String,

    @Schema(description = "Type of the dog", example = "포메라니안")
    val type: String,

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
    val dogDetails: DogDetails? = null,
) {
    data class DogDetails(
        val dogDescription: String,
        val foodBrand: String,
        val foodName: String,
        val foodType: String,
    )
}

