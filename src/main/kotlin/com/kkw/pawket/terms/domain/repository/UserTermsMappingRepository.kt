package com.kkw.pawket.terms.domain.repository

import com.kkw.pawket.terms.domain.UserTermsMapping
import com.kkw.pawket.terms.domain.UserTermsMappingId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface UserTermsMappingRepository: JpaRepository<UserTermsMapping, UserTermsMappingId> {
    fun findAllByUserIdAndTermsIdIn(userId: String, termsIdList: List<String>): List<UserTermsMapping>

    fun findAllByUserId(userId: String): List<UserTermsMapping>
}