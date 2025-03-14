package com.kkw.petwalker.user.model.res

data class LoginUserRes(
    val email: String,
    val provider: String,
    val token: String
)
