package com.kkw.pawket.pet.domain

import com.kkw.pawket.common.domain.BaseDateTimeEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "dog_details_history")
data class PetDetailsHistory (
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id", columnDefinition = "CHAR(36)")
    val pet: Pet,

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
        pet: Pet,
        dogDescription: String,
        foodBrand: String,
        foodName: String,
        foodType: String,
    ) : this(
        id = UUID.randomUUID().toString(),
        pet = pet,
        dogDescription = dogDescription,
        foodBrand = foodBrand,
        foodName = foodName,
        foodType = foodType,
    )
}