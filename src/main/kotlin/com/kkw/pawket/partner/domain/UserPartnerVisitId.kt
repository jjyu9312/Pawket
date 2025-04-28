package com.kkw.pawket.partner.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class UserPartnerVisitId(
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    val userId: String,

    @Column(name = "partner_id", columnDefinition = "CHAR(36)")
    val partnerId: String
) : Serializable
