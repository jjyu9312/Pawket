package com.kkw.petwalker.reservation

data class walkCourse (
    val id: Long,
    val pet: String,
    val walker: String,
    val date: String,
    val time: String,
    val duration: Int,
    val status: String,
)