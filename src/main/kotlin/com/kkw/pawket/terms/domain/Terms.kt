package com.kkw.pawket.terms.domain

import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Terms(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false)
    val isRequired: Boolean,

    ) : BaseEntity() {

    constructor(title: String, content: String, isRequired: Boolean) : this(
        id = UUID.randomUUID().toString(),
        title = title,
        content = content,
        isRequired = isRequired
    )
}