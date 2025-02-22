package com.kkw.petwalker.ads

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Company(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val name: String,

    ) : BaseEntity() {
    constructor(
        name: String,
    ) : this(
        id = UUID.randomUUID().toString(),
        name = name,
    )
}
