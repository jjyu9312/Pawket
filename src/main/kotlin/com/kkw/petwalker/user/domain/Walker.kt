package com.kkw.petwalker.user.domain

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Walker(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(columnDefinition = "CHAR(36)")
    val userId: String,

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    val gender: Gender,

    @Column(nullable = false)
    val imageUrl: String,

    @Column(nullable = false)
    val isExperiencedWithPets: Boolean = false,

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    val petCareExperience: String,
): BaseEntity()
