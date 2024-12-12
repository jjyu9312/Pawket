package com.kkw.petwalker.insurance.domain

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Insurance (
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false)
    val isRequired: Boolean = true,

): BaseEntity() {
    constructor(name: String, content: String, isRequired: Boolean) : this(
        id = UUID.randomUUID().toString(),
        name = name,
        content = content,
        isRequired = isRequired
    )
}
