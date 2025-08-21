package com.kkw.pawket.post.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.user.domain.User
import jakarta.persistence.*
import java.util.*

@Entity
data class DogShorts(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @JoinColumn(name = "user_id", columnDefinition = "CHAR(36)")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    @Column(nullable = false)
    val dogId: String,

    @Column(nullable = false)
    val title: String,

    @Column(nullable = false)
    val content: String,

    @Column(nullable = false)
    val videoPath: String,

    @Column(nullable = false)
    var likeCount: Int = 0,

    ) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as DogShorts
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
            dogId: String,
            title: String,
            content: String,
            videoPath: String,
        ): DogShorts = DogShorts(
            user = user,
            dogId = dogId,
            title = title,
            content = content,
            videoPath = videoPath,
        )
    }
}
