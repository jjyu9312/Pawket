package com.kkw.petwalker.walkRecord.domain.repository

import com.kkw.petwalker.walkRecord.domain.WalkRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalkRecordRepository: JpaRepository<WalkRecord, String> {
}