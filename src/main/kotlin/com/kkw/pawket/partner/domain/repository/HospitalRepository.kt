package com.kkw.pawket.partner.domain.repository

import com.kkw.pawket.partner.domain.Partner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HospitalRepository: JpaRepository<Partner, String> {
}