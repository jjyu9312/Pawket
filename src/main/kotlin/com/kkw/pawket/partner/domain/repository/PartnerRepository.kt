package com.kkw.pawket.partner.domain.repository

import com.kkw.pawket.partner.domain.Partner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PartnerRepository: JpaRepository<Partner, String> {
    fun findByIdAndIsDeletedFalse(id: String): Partner?
    fun findAllByUserId(userId: String): List<Partner>
    fun findAllByUserIdAndIsDeletedFalse(userId: String): List<Partner>
}