package com.kkw.petwalker.common.test

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController {

    @GetMapping("/test")
    fun test() {
        println("Test")
    }

    @GetMapping("/health")
    fun health() {
        println("Health")
    }
}