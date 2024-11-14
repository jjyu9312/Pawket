package com.kkw.petwalker.pet

data class Pet(
    val id: Long,
    val name: String,
    val species: String,
    val breed: String,
    val age: Int,
    val weight: Double,
    val owner: String,
)