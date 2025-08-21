package com.kkw.pawket.dog.model.req

import io.swagger.v3.oas.annotations.media.Schema

data class CreateDogReq(
    @Schema(description = "Registration number of the dog", example = "123456789")
    val registrationNum: String?,

    @Schema(description = "Name of the dog", example = "콩이")
    val name: String,

    @Schema(description = "Type of the dog", example = "포메라니안")
    val type: String,

    @Schema(description = "Age of the dog", example = "3")
    val age: Int,

    @Schema(description = "Weight of the dog ", example = "5")
    val weight: Int,

    @Schema(description = "Sex of the dog (male/female)", example = "male")
    val sex: String,

    @Schema(description = "neutered status of the dog", example = "true")
    val isNeutered: Boolean,

    @Schema(
        description = "dog details including description, food brand, name, and type",
        example = "{\"dogDescription\": \"Friendly and playful\", \"foodBrand\": \"BrandX\", \"foodName\": \"Premium Dog Food\", \"foodType\": \"Dry\"}"
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

