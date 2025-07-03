package com.kkw.pawket.partner.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
data class PartnerVisitHistory(
    @EmbeddedId
    val id: UserPartnerVisitId,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @MapsId("partnerId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id", columnDefinition = "CHAR(36)")
    val partner: Partner,

    @Column(nullable = false)
    val visitedAt: LocalDateTime = LocalDateTime.now()

) : BaseEntity()