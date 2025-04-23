package com.kkw.pawket.ads.service

import com.kkw.pawket.ads.domain.Company
import com.kkw.pawket.ads.domain.repository.AdsRepository
import com.kkw.pawket.ads.domain.repository.CompanyRepository
import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.partner.model.req.CreateCompanyReq
import com.kkw.pawket.user.domain.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AdsService(
    private val companyRepository: CompanyRepository,
    private val userRepository: UserRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun createCompany(userId: String, req: CreateCompanyReq): String {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(
                ResponseCode.USER_NOT_FOUND
            )

        val company = Company(
            user = user,
            name = req.name,
            addressBasic = req.addressBasic,
            addressLat = req.addressLat,
            addressLng = req.addressLng,
            addressDetail = req.addressDetail,
        )

        logger.info("Saving company: $company")

        companyRepository.save(company)

        return company.id
    }
}