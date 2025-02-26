package com.kkw.petwalker.feed.repository

import com.kkw.petwalker.feed.Feed
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository : JpaRepository<Feed, String> {
}