package com.kkw.petwalker.terms.domain.repository

import com.kkw.petwalker.terms.domain.UserTermsMapping
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository

interface UserTermsMappingRepository: JpaRepository<UserTermsMapping, String> {
}