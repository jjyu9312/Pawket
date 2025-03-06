package com.kkw.petwalker.user.domain

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
data class User(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String,

    @Column(nullable = false, length = 20)
    val name: String,

    @Column(nullable = false)
    val birth: LocalDate,

    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    val gender: Gender,

    @Column(nullable = false, length = 50, unique = true)
    val email: String,

    @Column(nullable = false)
    var totalCoin: Int = 0,

    @Column(nullable = false)
    val addressBasic: String,

    @Column(nullable = false)
    val addressLat: Double,

    @Column(nullable = false)
    val addressLng: Double,

    @Column()
    val addressDetail: String? = null,

    ) : BaseEntity() {
    constructor(
        name: String,
        birth: LocalDate,
        gender: Gender,
        email: String,
        addressBasic: String,
        addressLat: Double,
        addressLng: Double,
        addressDetail: String? = null,
    ) : this(
        id = UUID.randomUUID().toString(),
        name = name,
        birth = birth,
        gender = gender,
        email = email,
        addressBasic = addressBasic,
        addressLat = addressLat,
        addressLng = addressLng,
        addressDetail = addressDetail,
    )

    fun isValidEmail(): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        return email.matches(emailRegex.toRegex())
    }

    fun addCoin(coin: Int): Int {
        require(coin > 0) { "추가할 코인은 양수여야 합니다." }
        this.totalCoin += coin
        return this.totalCoin
    }

    fun subtractCoin(coin: Int): Int {
        require(coin > 0) { "차감할 코인은 양수여야 합니다." }
        require(this.totalCoin >= coin) { "보유 코인보다 많은 코인은 차감할 수 없습니다." }
        this.totalCoin -= coin
        return this.totalCoin
    }
}
