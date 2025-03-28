package com.kkw.pawket.user.domain

import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity
data class User(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String,

    @Column(nullable = false, length = 20)
    var name: String,

    @Column(nullable = false)
    var birth: LocalDate,

    @Column(nullable = false, length = 20)
    @Enumerated(value = EnumType.STRING)
    var gender: Gender,

    @Column(nullable = false, length = 50, unique = true)
    val email: String,

    @Column(nullable = false)
    var totalCoin: Int = 0,

    @Column(nullable = false)
    var addressBasic: String,

    @Column(nullable = false)
    var addressLat: Double,

    @Column(nullable = false)
    var addressLng: Double,

    @Column()
    var addressDetail: String? = null,

    ) : BaseEntity() {

    companion object {
        fun createWithEmailOnly(email: String): User = User(
            id = UUID.randomUUID().toString(),
            name = email,
            birth = LocalDate.now(),
            gender = Gender.MALE,
            email = email,
            addressBasic = "주소 미입력",
            addressLat = 0.0,
            addressLng = 0.0,
        )
    }

    fun update(
        name: String,
        birth: LocalDate,
        gender: Gender,
        addressBasic: String,
        addressLat: Double,
        addressLng: Double,
        addressDetail: String? = null
    ) {
        this.name = name
        this.birth = birth
        this.gender = gender
        this.addressBasic = addressBasic
        this.addressLat = addressLat
        this.addressLng = addressLng
        this.addressDetail = addressDetail
    }


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
