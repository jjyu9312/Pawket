package com.kkw.pawket.checklist.domain.repository

import com.kkw.pawket.checklist.domain.CheckType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckTypeRepository : JpaRepository<CheckType, String> {
}