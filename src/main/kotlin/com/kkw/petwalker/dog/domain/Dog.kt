package com.kkw.petwalker.dog.domain

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

    @Column(nullable = false, length = 50)
    val name: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val type: DogType,

    @Column(nullable = false) // ,로 연결
    val imageUrl: String,

    @Column(nullable = false)
    val age: Int,

    @Column(nullable = false)
    val weight: Int,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val sex: Sex,

    @Column(nullable = false)
    val isNeutered: Boolean = false,

): BaseEntity() {
    constructor(
        owner: Owner,
        name: String,
        type: DogType,
        imageUrl: String,
        age: Int,
        weight: Int,
        sex: Sex,
        isNeutered: Boolean,
    ) : this(
        id = UUID.randomUUID().toString(),
        owner = owner,
        type = type,
        name = name,
        imageUrl = imageUrl,
        age = age,
        weight = weight,
        sex = sex,
        isNeutered = isNeutered,
    )
}