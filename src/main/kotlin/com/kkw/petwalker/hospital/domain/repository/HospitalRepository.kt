package com.kkw.petwalker.hospital.domain.repository

import com.kkw.petwalker.hospital.domain.Hospital
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HospitalRepository: JpaRepository<Hospital, String> {
}