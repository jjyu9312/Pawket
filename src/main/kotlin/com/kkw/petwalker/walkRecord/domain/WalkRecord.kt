package com.kkw.petwalker.walkRecord.domain

import com.kkw.petwalker.common.domain.BaseEntity
import com.kkw.petwalker.user.domain.User
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class WalkRecord(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @Column(columnDefinition = "CHAR(36)")
    val dogId: String,

    @Column(nullable = false)
    val startDateTime: LocalDateTime,

    @Column(nullable = false)
    val endDateTime: LocalDateTime,

): BaseEntity() {
    constructor(
        user: User,
        dogId: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ) : this(
        id = UUID.randomUUID().toString(),
        user = user,
        dogId = dogId,
        startDateTime = startDateTime,
        endDateTime = endDateTime,
    )
}