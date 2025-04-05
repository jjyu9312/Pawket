package com.kkw.pawket.user.domain

import com.kkw.pawket.common.domain.BaseDateTimeEntity
import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
@Table(
    name = "user_oauth", uniqueConstraints = [
        UniqueConstraint(columnNames = ["provider", "providerUserId"])
    ]
)
class UserOAuth(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val provider: OAuthProvider,  // GOOGLE, KAKAO, APPLE

    @Column(nullable = false)
    val providerUserId: String,  // 구글: sub / 카카오: id / 애플: sub

) : BaseDateTimeEntity() {

    constructor(
        user: User,
        provider: OAuthProvider,
        providerUserId: String,
    ) : this(
        id = UUID.randomUUID().toString(),
        user = user,
        email = user.email,
        provider = provider,
        providerUserId = providerUserId,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UserOAuth
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
