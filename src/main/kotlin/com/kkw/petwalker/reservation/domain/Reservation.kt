package com.kkw.petwalker.reservation.domain

data class Reservation(
    val id: Long,
    val pet: String,
    val walker: String,
    val date: String,
    val time: String,
    val duration: Int,
    val status: String,
)