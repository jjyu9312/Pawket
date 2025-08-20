package com.kkw.pawket.walkRecord.domain

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
class WalkRecord(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @Column(columnDefinition = "CHAR(36)")
    val dogId: String,

    @Column(nullable = false)
    val startedAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = true)
    var finishedAt: LocalDateTime? = null,

    @Column(nullable = true, columnDefinition = "TEXT")
    var walkLocation: String? = null,

    @Column(nullable = true, length = 1000)
    var notes: String? = null,

): BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as WalkRecord
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            user: User,
            dogId: String,
            notes: String? = null,
        ): WalkRecord = WalkRecord(
            id = UUID.randomUUID().toString(),
            user = user,
            dogId = dogId,
            startedAt = LocalDateTime.now(),
            notes = notes,
        )
    }

    fun update(
        finishedAt: LocalDateTime,
        walkLocation: String,
    ) {
        this.finishedAt = finishedAt
        this.walkLocation = walkLocation
    }
}