package com.kkw.petwalker.notice.domain.repository

import com.kkw.petwalker.notice.domain.Notice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoticeRepository: JpaRepository<Notice, String> {
}