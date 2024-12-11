package com.kkw.petwalker.dog.domain

import com.kkw.petwalker.common.domain.BaseEntity
import com.kkw.petwalker.user.domain.Owner
import jakarta.persistence.*
import java.util.*

@Entity
data class Dog(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", columnDefinition = "CHAR(36)")
    val owner: Owner,

    @Column(nullable = false)
    val imageUrl: String,

    @Column(nullable = false)
    val age: Int,

    @Column(nullable = false)
    val weight: Int,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val sex: Sex,

    @Column(nullable = false)
    val isNeutered: Boolean = false,

): BaseEntity()