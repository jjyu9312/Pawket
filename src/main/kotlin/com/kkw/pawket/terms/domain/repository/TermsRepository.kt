package com.kkw.pawket.terms.domain.repository

import com.kkw.pawket.terms.domain.Terms
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TermsRepository : JpaRepository<Terms, String>, TermsRepositoryCustom {
    fun findAllByIsRequiredTrue(): List<Terms>

    fun findByIdAndIsDeletedFalse(id: String): Terms?
}