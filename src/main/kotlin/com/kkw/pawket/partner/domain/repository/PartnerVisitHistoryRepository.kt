package com.kkw.pawket.partner.domain.repository

import com.kkw.pawket.partner.domain.Partner
import com.kkw.pawket.partner.domain.PartnerVisitHistory
import com.kkw.pawket.partner.domain.UserPartnerVisitId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PartnerVisitHistoryRepository: JpaRepository<PartnerVisitHistory, UserPartnerVisitId> {
    fun findAllByUserId(userId: String): List<PartnerVisitHistory>

}