package com.kkw.pawket.spot.service

import com.kkw.pawket.ads.repository.CompanyRepository
import com.kkw.pawket.spot.domain.ImportanceLevel
import com.kkw.pawket.spot.domain.Spot
import com.kkw.pawket.spot.domain.repository.SpotRepository
import com.kkw.pawket.spot.model.req.CreateSpotReq
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service

@Service
class SpotService
    (
    private val spotRepository: SpotRepository,
    private val companyRepository: CompanyRepository,
) {
    fun createSpot(companyId: String, req: CreateSpotReq): String? {
        val company = companyRepository.findById(companyId)
            .orElseThrow { BadRequestException("Company not found") }

        val spot = Spot(
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