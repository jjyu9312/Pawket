package com.kkw.pawket.pet.model.req

import com.kkw.pawket.user.model.req.CreateUserReq
import org.springframework.web.multipart.MultipartFile

data class CreatePetReq(
    val registrationNum: String?,
    val name: String,
    val type: String,
    val dogType: String?,
    val mainImageUrl: MultipartFile?,
    val imageUrls: List<MultipartFile>?,
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

