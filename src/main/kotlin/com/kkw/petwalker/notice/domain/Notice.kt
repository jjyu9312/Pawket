package com.kkw.petwalker.notice.domain

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Notice (
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    val content: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val targetGroup: TargetGroup,

    @Column(nullable = false)
    val isRequired: Boolean = false,

    @Column(nullable = false)
    val priority: Int,
): BaseEntity()