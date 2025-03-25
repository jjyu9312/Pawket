package com.kkw.petwalker.ads

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Company(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val addressBasic: String,

    @Column(nullable = false)
    val addressLat: Double,

    @Column(nullable = false)
    val addressLng: Double,

    @Column(nullable = false)
    val addressDetail: String,

    ) : BaseEntity() {
    constructor(
        name: String,
        addressBasic: String,
        addressLat: Double,
        addressLng: Double,
        addressDetail: String,

    ) : this(
        id = UUID.randomUUID().toString(),
        name = name,
        addressBasic = addressBasic,
        addressLat = addressLat,
        addressLng = addressLng,
        addressDetail = addressDetail,
    )
}
