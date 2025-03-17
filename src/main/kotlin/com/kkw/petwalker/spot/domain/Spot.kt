package com.kkw.petwalker.spot.domain

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity(name = "animal_hospital")
data class Spot(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val details: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val importanceLevel: ImportanceLevel = ImportanceLevel.LOW,

    @Column(nullable = false)
    val lat: Double,

    @Column(nullable = false)
    val lng: Double,

    ) : BaseEntity() {

    constructor(
        name: String,
        details: String,
        importanceLevel: ImportanceLevel,
        lat: Double,
        lng: Double,
    ) : this(
        id = UUID.randomUUID().toString(),
        name = name,
        details = details,
        importanceLevel = importanceLevel,
        lat = lat,
        lng = lng,
    )
}