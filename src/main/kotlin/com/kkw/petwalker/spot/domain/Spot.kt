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
        details: String,
        importanceLevel: ImportanceLevel,
        addressBasic: String,
        addressLat: Double,
        addressLng: Double,
        addressDetail: String? = null,
    ) : this(
        id = UUID.randomUUID().toString(),
        name = name,
        details = details,
        importanceLevel = importanceLevel,
        addressBasic = addressBasic,
        addressLat = addressLat,
        addressLng = addressLng,
        addressDetail = addressDetail,
    )
}