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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Terms
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            title: String,
            content: String,
            isRequired: Boolean,
        ): Terms = Terms(
            title = title,
            content = content,
            isRequired = isRequired,
        )
    }
}