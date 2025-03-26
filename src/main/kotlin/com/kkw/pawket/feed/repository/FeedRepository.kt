package com.kkw.pawket.feed.repository

import com.kkw.pawket.feed.Feed
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FeedRepository : JpaRepository<Feed, String> {
}