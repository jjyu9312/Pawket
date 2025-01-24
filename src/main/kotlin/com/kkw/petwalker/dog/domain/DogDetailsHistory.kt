package com.kkw.petwalker.dog.domain

import com.kkw.petwalker.common.domain.BaseDateTimeEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "dog_details_history")
data class DogDetailsHistory (
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id", columnDefinition = "CHAR(36)")
    val dog: Dog,

    @Column(nullable = false)
    val dogDescription: String,

    @Column(nullable = false, length = 50)
    val foodBrand: String,

    @Column(nullable = false, length = 50)
    val foodName: String,

    @Column(nullable = false, length = 50)
    val foodType: String,
): BaseDateTimeEntity() {
    constructor(
        dog: Dog,
        dogDescription: String,
        foodBrand: String,
        foodName: String,
        foodType: String,
    ) : this(
        id = UUID.randomUUID().toString(),
        dog = dog,
        dogDescription = dogDescription,
        foodBrand = foodBrand,
        foodName = foodName,
        foodType = foodType,
    )
}