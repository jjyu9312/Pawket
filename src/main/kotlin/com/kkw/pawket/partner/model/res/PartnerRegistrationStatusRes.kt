package com.kkw.pawket.partner.model.res

data class PartnerRegistrationStatusRes (
    val isRegistered: Boolean,
    val partners: List<PartnerInfo>,
)

data class PartnerInfo(
    val id: String,
    val name: String,
    val ownerName: String,
    val contactNumber: String,
    val link: String? = null,
)
