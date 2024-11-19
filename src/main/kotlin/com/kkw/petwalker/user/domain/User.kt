package com.kkw.petwalker.user.domain

import jakarta.persistence.*
import java.time.LocalDate
import java.util.UUID
import java.util.regex.Pattern

@Entity
data class User(

    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, length = 20)
    val name: String,

    @Column(nullable = false)
    val birth: LocalDate,

    @Column(nullable = false, length = 50)
    val email: String,

    @Column(nullable = false)
    val addressBasic: String,

    @Column(nullable = false)
    val addressLat: Float,

    @Column(nullable = false)
    val addressLng: Float,

    @Column
    val addressDetail: String,

) {
    fun isValidEmail(): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        val pattern = Pattern.compile(emailRegex)
        return pattern.matcher(email).matches()
    }
}
