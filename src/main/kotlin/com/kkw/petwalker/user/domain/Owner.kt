package com.kkw.petwalker.user.domain

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.UUID

@Entity
data class Owner (

    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(columnDefinition = "CHAR(36)")
    val userId: String,
): BaseEntity()
