package com.kkw.petwalker.user.dto

import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

class CreateWalkerDto {

    data class Req (
        val name: String,
        val birth: LocalDate,
        val email: String,
        val gender: String,
        val imageUrl: MultipartFile,
        val addressInfo: AddressInfo,
        val walkerInfo: WalkerInfo,

    ) {
        data class AddressInfo (
            val basic: String,
            val detail: String,
            val lat: Double,
            val lng: Double
        )

        data class WalkerInfo (
            val isExperiencedWithPets: Boolean,
            val petCareExperience: String,
        )
    }

    data class Res (
        val userId: String
    )
}
