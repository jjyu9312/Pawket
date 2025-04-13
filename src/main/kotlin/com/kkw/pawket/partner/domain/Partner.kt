package com.kkw.pawket.partner.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity
data class Partner(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    val user: User,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    val ownerName: String,

    @Column(nullable = false, length = 50)
    val contactNumber: String,

    @Column(nullable = true)
    val link: String,

    ) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Partner
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            user: User,
            name: String,
            ownerName: String,
            contactNumber: String,
            link: String,
        ): Partner {
            return Partner(
                user = user,
                name = name,
                ownerName = ownerName,
                contactNumber = contactNumber,
                link = link,
            )
        }
    }
}