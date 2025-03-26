package com.kkw.pawket.user.model.res

data class LoginUserRes(
    val email: String,
    val provider: String,
    val token: String
)
