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

    @PostMapping("/login")
    fun loginPage(): String {
        return "login"
    }

    @PostMapping("/logout")
    fun logout() {
        println("User logged out")
    }
}