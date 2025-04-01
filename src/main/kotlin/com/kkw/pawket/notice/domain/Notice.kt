package com.kkw.pawket.notice.domain

import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Notice(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    val targetGroup: TargetGroup = TargetGroup.ALL,

    @Column(nullable = false)
    val isRequired: Boolean = false,

    @Column(nullable = false)
    val priority: Int = 0,

    ) : BaseEntity() {

    constructor(
        title: String,
        content: String,
        targetGroup: TargetGroup,
        isRequired: Boolean,
        priority: Int
    ) : this(
        id = UUID.randomUUID().toString(),
        title = title,
        content = content,
        targetGroup = targetGroup,
        isRequired = isRequired,
        priority = priority
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Notice
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}