package com.kkw.petwalker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PetWalkerApplication

fun main(args: Array<String>) {
    runApplication<PetWalkerApplication>(*args)
}
