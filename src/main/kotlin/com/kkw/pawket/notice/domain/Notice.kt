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
    var title: String,

    @Column(nullable = false, columnDefinition = "TEXT")
    var content: String,

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    val targetGroup: TargetGroup = TargetGroup.ALL,

    @Column(nullable = false)
    var isRequired: Boolean = false,

    @Column(nullable = false)
    var priority: Int = 0,

    ) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Notice
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            title: String,
            content: String,
            targetGroup: TargetGroup,
            isRequired: Boolean,
            priority: Int
        ): Notice = Notice(
            id = UUID.randomUUID().toString(),
            title = title,
            content = content,
            targetGroup = targetGroup,
            isRequired = isRequired,
            priority = priority
        )

        fun update(
            notice: Notice,
            title: String,
            content: String,
            isRequired: Boolean,
            priority: Int
        ): Notice = notice.copy(
            title = title,
            content = content,
            isRequired = isRequired,
            priority = priority
        )
    }
}