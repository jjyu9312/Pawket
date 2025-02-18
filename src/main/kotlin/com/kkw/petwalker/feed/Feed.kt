package com.kkw.petwalker.feed

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Feed(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val content: String,

    ) : BaseEntity() {
    constructor(
        name: String,
        content: String
    ) : this(
        id = UUID.randomUUID().toString(),
        name = name,
        content = content,
    )
}
