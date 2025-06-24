package com.kkw.pawket.ai.model.res

import com.kkw.pawket.ai.model.common.PetProfile

// Pet Profile - Response
data class PetProfileRes(
    val profile: PetProfile,
    val createdAt: Long? = null,
    val updatedAt: Long? = null
)