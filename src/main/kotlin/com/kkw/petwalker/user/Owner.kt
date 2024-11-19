package com.kkw.petwalker.user

data class Owner(
    val id: Long,
    val name: String,
    val email: String,
    val password: String,
    val role: String,
    val enabled: Boolean,
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val credentialsNonExpired: Boolean,
)
