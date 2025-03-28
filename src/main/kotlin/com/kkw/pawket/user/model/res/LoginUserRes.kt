package com.kkw.pawket.user.model.res

data class LoginUserRes(
    val id: String,
    val provider: String,
    val token: String
)
