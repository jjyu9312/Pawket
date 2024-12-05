package com.kkw.petwalker.insurance.domain

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*

@Entity
data class Insurance (
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false)
    val isRequired: Boolean = true,

): BaseEntity()
