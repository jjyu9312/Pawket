package com.kkw.pawket.pet.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity
data class Pet(
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

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    val type: PetType,

    @Column(nullable = true, length = 50)
    @Enumerated(value = EnumType.STRING)
    val dogType: DogType? = null,

    @Column(nullable = true, unique = true)
    var mainImagePath: String? = null,

    @Column(nullable = true, unique = true) // ,로 연결
    var imagePaths: String? = null,

    @Column(nullable = false)
    val age: Int,

    @Column(nullable = false)
    var weight: Int,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    val sex: Sex,

    @Column(nullable = false)
    var isNeutered: Boolean = false,

    @Column(nullable = false, columnDefinition = "TEXT") // JSON 데이터 저장
    var petDetail: String? = null,

    ) : BaseEntity() {



    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Pet
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            user: User,
            name: String,
            type: PetType,
            dogType: DogType? = null,
            mainImagePath: String? = null,
            imagePaths: String? = null,
            age: Int,
            weight: Int,
            sex: Sex,
            isNeutered: Boolean = false,
            registrationNum: String? = null
        ): Pet = Pet(
            id = UUID.randomUUID().toString(),
            user = user,
            name = name,
            type = type,
            dogType = dogType,
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