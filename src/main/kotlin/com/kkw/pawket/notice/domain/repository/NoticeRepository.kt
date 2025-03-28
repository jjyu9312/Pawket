package com.kkw.pawket.notice.domain.repository

import com.kkw.pawket.notice.domain.Notice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface NoticeRepository: JpaRepository<Notice, String> {
}