package com.kkw.pawket.pet.model.req

data class CreatePetReq(
    val registrationNum: String?,
    val name: String,
    val type: String,
    val dogType: String?,
    val mainImageUrl: String?,
    val imageUrls: String?,
    val age: Int,
    val weight: Int,
    val sex: String,
    val isNeutered: Boolean,
    val petDescription: String,
    val foodBrand: String,
    val foodName: String,
    val foodType: String,
)
