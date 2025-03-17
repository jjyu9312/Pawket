package com.kkw.petwalker.terms.domain.repository

import com.kkw.petwalker.terms.domain.Terms
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface TermsRepositoryCustom {
    fun findRequiredTermsNotAgreedByUser(userId: String): List<Terms>
}