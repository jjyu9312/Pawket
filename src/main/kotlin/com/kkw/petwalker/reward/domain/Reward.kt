package com.kkw.petwalker.reward.domain

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
abstract class Reward(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val coin: Int,
) : BaseEntity()