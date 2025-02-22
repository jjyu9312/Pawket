package com.kkw.petwalker.ads.repository

import com.kkw.petwalker.ads.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository: JpaRepository<Company, String> {
}