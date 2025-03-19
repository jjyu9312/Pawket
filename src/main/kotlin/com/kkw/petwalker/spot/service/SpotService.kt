package com.kkw.petwalker.spot.service

import com.kkw.petwalker.ads.repository.CompanyRepository
import com.kkw.petwalker.spot.domain.ImportanceLevel
import com.kkw.petwalker.spot.domain.Spot
import com.kkw.petwalker.spot.domain.repository.SpotRepository
import com.kkw.petwalker.spot.model.req.CreateSpotReq
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