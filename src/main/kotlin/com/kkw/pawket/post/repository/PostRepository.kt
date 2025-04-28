package com.kkw.pawket.post.repository

import com.kkw.pawket.post.domain.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, String> {
    fun findAllByUserId(userId: String): List<Post>
}