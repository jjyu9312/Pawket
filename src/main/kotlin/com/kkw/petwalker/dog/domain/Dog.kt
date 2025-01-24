package com.kkw.petwalker.dog.domain

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.kkw.petwalker.common.domain.BaseEntity
import com.kkw.petwalker.user.domain.Owner
import jakarta.persistence.*
import java.util.*

@Entity
data class Dog(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", columnDefinition = "CHAR(36)")
    val owner: Owner,

    @Column(nullable = false, length = 50, unique = true)
    val registrationNum: String,

    @Column(nullable = false, length = 50)
    val name: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val type: DogType,

    @Column(nullable = false, unique = true)
    val mainImageUrl: String,

    @Column(nullable = false) // ,로 연결
    val imageUrls: String,

    @Column(nullable = false)
    val age: Int,

    @Column(nullable = false)
    val weight: Int,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val sex: Sex,

    @Column(nullable = false)
    val isNeutered: Boolean = false,

    @Column(nullable = false, columnDefinition = "TEXT") // JSON 데이터 저장
    val dogDetail: String,

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
                "dog_description" to dogDescription,
                "food_brand" to foodBrand,
                "food_name" to foodName,
                "food_type" to foodType
            )
            return objectMapper.writeValueAsString(detailMap)
        }
    }

    constructor(
        owner: Owner,
        registrationNum: String,
        name: String,
        type: DogType,
        mainImageUrl: String,
        imageUrls: String,
        age: Int,
        weight: Int,
        sex: Sex,
        isNeutered: Boolean,
        dogDescription: String,
        foodBrand: String,
        foodName: String,
        foodType: String,
    ) : this(
        id = UUID.randomUUID().toString(),
        owner = owner,
        registrationNum = registrationNum,
        name = name,
        type = type,
        mainImageUrl = mainImageUrl,
        imageUrls = imageUrls,
        age = age,
        weight = weight,
        sex = sex,
        isNeutered = isNeutered,
        dogDetail = createDogDetailJson(dogDescription, foodBrand, foodName, foodType) // JSON 변환 후 저장
    )
}