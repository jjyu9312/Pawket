package com.kkw.pawket.checklist.domain.repository

import com.kkw.pawket.checklist.domain.Checklist
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChecklistRepository : JpaRepository<Checklist, String> {
}