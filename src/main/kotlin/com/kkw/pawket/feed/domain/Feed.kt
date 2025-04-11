package com.kkw.pawket.feed.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity
data class Feed(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @Column(nullable = false)
    val petId: String,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false)
    var likeCount: Int = 0,

    ) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Feed
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    fun updateLikeCnt(type: String) {
        if (type == "like")
            this.likeCount += 1
        else if (type == "dislike")
            this.likeCount -= 1
    }

    companion object {
        fun create(
            user: User,
            petId: String,
            title: String,
            content: String,
        ): Feed = Feed(
            user = user,
            petId = petId,
            title = title,
            content = content,
        )
    }
}
