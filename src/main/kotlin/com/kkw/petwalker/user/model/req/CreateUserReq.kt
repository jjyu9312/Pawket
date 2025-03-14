package com.kkw.petwalker.user.model.req

import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

data class CreateUserReq (
    val name: String,
    val birth: LocalDate,
    val email: String,
    val gender: String,
    val imageUrl: MultipartFile,
    val addressInfo: AddressInfo,
    val petInfo: PetInfo? = null,

    ) {
    data class AddressInfo (
        val basic: String,
        val detail: String? = null,
        val lat: Double,
        val lng: Double
    )

    data class PetInfo (
        val registrationNum: String? = null,
        val name: String,
        val type: String,
        val dogType: String? = null,
        val imageUrls: List<MultipartFile>,
        val age: Int,
        val sex: String,
        val weight: Int,
        val isNeutered: Boolean,
        val dogDescription: String,
        val foodBrand: String,
        val foodName: String,
        val foodType: String,
    )
}
