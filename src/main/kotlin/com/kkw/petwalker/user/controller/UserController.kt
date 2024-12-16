package com.kkw.petwalker.user.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
    @PostMapping
    fun createUser() {
        println("User created")
    }

    @PotMapping("/login")
    fun loginPage(): String {
        return "login" // login.html 템플릿 렌더링
    }

    @PostMapping("/logout")
    fun logout() {
        println("User logged out")
    }
}