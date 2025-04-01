package com.kkw.pawket.partner.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class PartnerVistHistory(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", columnDefinition = "CHAR(36)")
    val partner: Partner,

    @Column(nullable = false)
    val visitedAt: LocalDateTime = LocalDateTime.now()

    ) : BaseEntity() {

    constructor(
        user: User,
        partner: Partner,
        visitedAt: LocalDateTime,
    ) : this(
        id = UUID.randomUUID().toString(),
        user = user,
        partner = partner,
        visitedAt = visitedAt,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PartnerVistHistory
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}