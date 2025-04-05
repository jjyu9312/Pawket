package com.kkw.pawket.walkRecord.domain.repository

import com.kkw.pawket.walkRecord.domain.WalkRecord
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalkRecordRepository: JpaRepository<WalkRecord, String> {
    fun findAllByUserId(userId: String): List<WalkRecord>
    fun findByIdAndIsDeletedFalse(id: String): WalkRecord?
}