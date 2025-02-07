package com.kkw.petwalker.user.dto

import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

class CreateUserDto {

    data class Req (
        val name: String,
        val birth: LocalDate,
        val email: String,
        val gender: String,
        val imageUrl: MultipartFile,
        val addressInfo: AddressInfo,
        val dogInfo: DogInfo,

    ) {
        data class AddressInfo (
            val basic: String,
            val detail: String,
            val lat: Double,
            val lng: Double
        )

        data class DogInfo (
            val registrationNum: String? = null,
            val name: String,
            val type: String,
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

    data class Res (
        val userId: String
    )
}
