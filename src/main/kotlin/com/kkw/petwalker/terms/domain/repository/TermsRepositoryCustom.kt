package com.kkw.pawket.terms.domain.repository

import com.kkw.pawket.terms.domain.Terms
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface TermsRepositoryCustom {
    fun findRequiredTermsNotAgreedByUser(userId: String): List<Terms>
}