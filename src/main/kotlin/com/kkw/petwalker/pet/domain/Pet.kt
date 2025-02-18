package com.kkw.petwalker.pet.domain

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kkw.petwalker.common.domain.BaseEntity
import com.kkw.petwalker.user.domain.User
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

    @Column(nullable = true)
    @Enumerated(value = EnumType.STRING)
    val dogType: DogType? = null,

    @Column(nullable = true, unique = true)
    var mainImageUrl: String? = null,

    @Column(nullable = true, unique = true) // ,로 연결
    var imageUrls: String? = null,

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
    var dogDetail: String,

    ) : BaseEntity() {
    companion object {
        private val objectMapper = jacksonObjectMapper()

        // Dog 상세 정보를 JSON으로 변환
        fun createDogDetailJson(
            dogDescription: String,
            foodBrand: String,
            foodName: String,
            foodType: String
        ): String {
            val detailMap = mapOf(
                "dogDescription" to dogDescription,
                "foodBrand" to foodBrand,
                "foodName" to foodName,
                "foodType" to foodType
            )
            return objectMapper.writeValueAsString(detailMap)
        }
    }

    constructor(
        user: User,
        registrationNum: String? = null,
        name: String,
        type: PetType,
        dogType: DogType? = null,
        mainImageUrl: String? = null,
        imageUrls: String? = null,
        age: Int,
        sex: Sex,
        weight: Int,
        isNeutered: Boolean,
        dogDescription: String,
        foodBrand: String,
        foodName: String,
        foodType: String,
    ) : this(
        id = UUID.randomUUID().toString(),
        user = user,
        registrationNum = registrationNum,
        name = name,
        type = type,
        dogType = dogType,
        mainImageUrl = mainImageUrl,
        imageUrls = imageUrls,
        age = age,
        sex = sex,
        weight = weight,
        isNeutered = isNeutered,
        dogDetail = createDogDetailJson(
            dogDescription,
            foodBrand,
            foodName,
            foodType
        ) // JSON 변환 후 저장
    )
}