package com.kkw.pawket.common.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/common")
class CommonController {
    @GetMapping("/health-check")
    fun healthCheck(): String {
        return "OK"
    }
}