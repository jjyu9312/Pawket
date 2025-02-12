package com.kkw.petwalker.user.dto

data class LoginUserDto(
    val email: String,
    val provider: String,
    val token: String
)
