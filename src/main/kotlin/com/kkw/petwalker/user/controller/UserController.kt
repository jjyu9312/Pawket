package com.kkw.petwalker.user.controller

import com.kkw.petwalker.user.dto.CreateOwnerDto
import com.kkw.petwalker.user.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/user")
class UserController (
    private val userService: UserService,
) {

    @PostMapping("/login")
    fun loginPage(): String = userService.login()

    @PostMapping("/logout")
    fun logout() = userService.logout()

    @PostMapping("/owner")
    fun createOwner(req: CreateOwnerDto.Req) = userService.createOwner(req)

    @PostMapping("/walker")
    fun createWalker(req: CreateOwnerDto.Req) = userService.createWalker(req)


}