package com.kkw.petwalker.walker

data class walker(
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
