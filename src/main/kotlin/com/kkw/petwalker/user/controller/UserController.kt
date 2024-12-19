package com.kkw.petwalker.user.controller

import com.kkw.petwalker.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {
    @PostMapping
    fun createUser() {
        println("User created")
    }

    @PostMapping("/login")
    fun loginPage(): String = userService.login()

    @PostMapping("/logout")
    fun logout() = userService.logout()
}