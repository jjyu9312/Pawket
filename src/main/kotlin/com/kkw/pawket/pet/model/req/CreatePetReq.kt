package com.kkw.pawket.pet.model.req

import com.kkw.pawket.user.model.req.CreateUserReq

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
    val petDetails: PetDetails? = null,
) {
    data class PetDetails(
        val petDescription: String,
        val foodBrand: String,
        val foodName: String,
        val foodType: String,
    )
}

