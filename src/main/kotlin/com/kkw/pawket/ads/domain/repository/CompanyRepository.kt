package com.kkw.pawket.ads.domain.repository

import com.kkw.pawket.ads.domain.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository: JpaRepository<Company, String> {
    fun findByIdAndIsDeletedFalse(id: String): Company?
    fun findAllByUserId(userId: String): List<Company>
}