package com.kkw.pawket.ads

import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class Ads(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @JoinColumn(name = "company_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val company: Company,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = true)
    val content: String? = null,

    @Column(nullable = true)
    val coin: Int = 0,

    ) : BaseEntity() {
    constructor(
        company: Company,
        title: String,
        content: String?,
        coin: Int,
    ) : this(
        id = UUID.randomUUID().toString(),
        company = company,
        title = title,
        content = content,
        coin = coin,
    )
}
