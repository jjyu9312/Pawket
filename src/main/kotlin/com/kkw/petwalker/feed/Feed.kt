package com.kkw.petwalker.feed

import com.kkw.petwalker.common.domain.BaseEntity
import com.kkw.petwalker.user.domain.User
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
    constructor(
        user: User,
        petId: String,
        title: String,
        content: String,
    ) : this(
        id = UUID.randomUUID().toString(),
        user = user,
        petId = petId,
        title = title,
        content = content,
    )

    fun updateLikeCnt(type: String) {
        if (type == "like")
            this.likeCount += 1
        else if (type == "dislike")
            this.likeCount -= 1
    }
}
