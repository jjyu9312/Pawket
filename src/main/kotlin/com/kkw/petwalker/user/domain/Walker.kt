package com.kkw.petwalker.user.domain

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Walker(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String,

    @Column(columnDefinition = "CHAR(36)")
    val userId: String,

    @Column(nullable = false)
    val isExperiencedWithPets: Boolean = false,

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    val petCareExperience: String,

): BaseEntity() {
    constructor(
        userId: String,
        isExperiencedWithPets: Boolean,
        petCareExperience: String,
    ): this(
        id = UUID.randomUUID().toString(),
        userId = userId,
        isExperiencedWithPets = isExperiencedWithPets,
        petCareExperience = petCareExperience,
    )

}
