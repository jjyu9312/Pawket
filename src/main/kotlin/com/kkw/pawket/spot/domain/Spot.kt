package com.kkw.pawket.spot.domain

import com.kkw.pawket.ads.domain.Company
import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Spot(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", columnDefinition = "CHAR(36)")
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Spot
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            company: Company,
            name: String,
            detail: String,
            importanceLevel: ImportanceLevel,
            addressBasic: String,
            addressLat: Double,
            addressLng: Double,
            addressDetail: String? = null,
        ): Spot = Spot(
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
}