package com.kkw.pawket.spot.service

import com.kkw.pawket.ads.domain.repository.CompanyRepository
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.spot.domain.ImportanceLevel
import com.kkw.pawket.spot.domain.Spot
import com.kkw.pawket.spot.domain.repository.SpotRepository
import com.kkw.pawket.spot.model.req.CreateSpotReq
import org.apache.coyote.BadRequestException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SpotService
    (
    private val spotRepository: SpotRepository,
    private val companyRepository: CompanyRepository,
) {
    private val logger = LoggerFactory.getLogger(SpotService::class.java)

    fun createSpot(companyId: String, req: CreateSpotReq): String {

        val company = companyRepository.findByIdAndIsDeletedFalse(companyId)
            ?: throw BadRequestException(
                ResponseCode.COMPANY_NOT_FOUND.defaultMessage
            )

        val spot = Spot.create(
            company = company,
            name = req.name,
            detail = req.detail,
            importanceLevel = ImportanceLevel.fromString(req.importanceLevel)!!,
            addressBasic = req.addressBasic,
            addressLat = req.addressLat,
            addressLng = req.addressLng,
            addressDetail = req.addressDetail,
        )

        spotRepository.save(spot)

        return spot.id
    }
}