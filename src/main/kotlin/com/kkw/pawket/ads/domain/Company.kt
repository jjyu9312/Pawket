package com.kkw.pawket.ads.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity
data class Company(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    var addressBasic: String,

    @Column(nullable = false)
    var addressLat: Double,

    @Column(nullable = false)
    var addressLng: Double,

    @Column(nullable = false)
    var addressDetail: String? = null,

    ) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Company
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            user: User,
            name: String,
            addressBasic: String,
            addressLat: Double,
            addressLng: Double,
            addressDetail: String?,
        ): Company = Company(
            user = user,
            name = name,
            addressBasic = addressBasic,
            addressLat = addressLat,
            addressLng = addressLng,
            addressDetail = addressDetail,
        )
    }
}
