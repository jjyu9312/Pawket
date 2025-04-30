package com.kkw.pawket.terms.domain

import com.kkw.pawket.common.domain.BaseDateTimeEntity
import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity
data class UserTermsMapping(

    @EmbeddedId
    val id: UserTermsMappingId,

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @MapsId("termsId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id", columnDefinition = "CHAR(36)")
    val terms: Terms,

    @Column(nullable = false)
    val isAgreed: Boolean = false

) : BaseDateTimeEntity()
