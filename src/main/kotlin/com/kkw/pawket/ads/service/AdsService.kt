package com.kkw.pawket.ads.service

import com.kkw.pawket.ads.domain.Company
import com.kkw.pawket.ads.domain.repository.AdsRepository
import com.kkw.pawket.ads.domain.repository.CompanyRepository
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.partner.model.req.CreateCompanyReq
import com.kkw.pawket.user.domain.repository.UserRepository
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service

@Service
class AdsService(
    private val adsRepository: AdsRepository,
    private val companyRepository: CompanyRepository,
    private val userRepository: UserRepository,
) {
    fun createCompany(userId: String, req: CreateCompanyReq): String {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(
                ResponseCode.USER_NOT_FOUND.defaultMessage
            )

        val company = Company(
            user = user,
            name = req.name,
            addressBasic = req.addressBasic,
            addressLat = req.addressLat,
            addressLng = req.addressLng,
            addressDetail = req.addressDetail,
        )

        companyRepository.save(company)

        return company.id
    }
}