package com.kkw.pawket.ads.repository

import com.kkw.pawket.ads.Company
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository: JpaRepository<Company, String> {
}