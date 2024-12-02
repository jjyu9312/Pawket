package com.kkw.petwalker.terms.domain

import com.kkw.petwalker.common.domain.BaseEntity
import com.kkw.petwalker.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity
data class UserTermsMapping (
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "terms_id", columnDefinition = "CHAR(36)")
    val terms: Terms,

    @Column(nullable = false)
    val isAgreed: Boolean = false,
): BaseEntity()