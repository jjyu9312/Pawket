package com.kkw.pawket.dog.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity
data class Dog(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @Column(nullable = true, length = 50, unique = true)
    var registrationNum: String? = null,

    @Column(nullable = false, length = 50)
    val name: String,

    @Column(nullable = true, length = 50)
    @Enumerated(value = EnumType.STRING)
    val type: DogType,

    @Column(nullable = true, unique = true)
    var mainImagePath: String? = null,

    @Column(nullable = true, unique = true) // ,로 연결
    var imagePaths: String? = null,

    @Column(nullable = false)
    var age: Int,

    @Column(nullable = false)
    var weight: Int,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    val sex: Sex,

    @Column(nullable = false)
    var isNeutered: Boolean = false,

    @Column(nullable = false, columnDefinition = "TEXT") // JSON 데이터 저장
    var dogDetail: String? = null,

    ) : BaseEntity() {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Dog
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            user: User,
            name: String,
            type: DogType,
            mainImagePath: String? = null,
            imagePaths: String? = null,
            age: Int,
            weight: Int,
            sex: Sex,
            isNeutered: Boolean = false,
            registrationNum: String? = null
        ): Dog = Dog(
            id = UUID.randomUUID().toString(),
            user = user,
            name = name,
            type = type,
            mainImagePath = mainImagePath,
            imagePaths = imagePaths,
            age = age,
            weight = weight,
            sex = sex,
            isNeutered = isNeutered,
            registrationNum = registrationNum,
        )
    }
}