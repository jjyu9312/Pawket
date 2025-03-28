package com.kkw.pawket.spot.domain

import com.kkw.pawket.ads.Company
import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Spot(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val company: Company,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val detail: String,

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
        company: Company,
        name: String,
        detail: String,
        importanceLevel: ImportanceLevel,
        addressBasic: String,
        addressLat: Double,
        addressLng: Double,
        addressDetail: String? = null,
    ) : this(
        id = UUID.randomUUID().toString(),
        company = company,
        name = name,
        detail = detail,
        importanceLevel = importanceLevel,
        addressBasic = addressBasic,
        addressLat = addressLat,
        addressLng = addressLng,
        addressDetail = addressDetail,
    )
}