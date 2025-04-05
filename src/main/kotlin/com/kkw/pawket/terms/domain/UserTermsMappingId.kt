package com.kkw.pawket.terms.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class UserTermsMappingId(
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    val userId: String,

    @Column(name = "terms_id", columnDefinition = "CHAR(36)")
    val termsId: String
) : java.io.Serializable
