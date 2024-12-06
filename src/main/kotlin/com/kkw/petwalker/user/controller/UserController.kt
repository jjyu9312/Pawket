package com.kkw.petwalker.user.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
    @PostMapping
    fun createUser() {
        println("User created")
    }
}