package com.kkw.pawket.partner.service

import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.partner.domain.Partner
import com.kkw.pawket.partner.domain.repository.PartnerRepository
import com.kkw.pawket.partner.model.req.CreatePartnerReq
import com.kkw.pawket.user.domain.repository.UserRepository
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service

@Service
class PartnerService(
    private val partnerRepository: PartnerRepository,
    private val userRepository: UserRepository,
) {
    fun createPartner(userId: String, req: CreatePartnerReq): String {
        val user = userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(
                ResponseCode.USER_NOT_FOUND.defaultMessage
            )

        val partner = Partner(
            user = user,
            name = req.name,
            ownerName = req.ownerName,
            contactNumber = req.contactNumber,
            link = req.link,
        )

        partnerRepository.save(partner)

        return partner.id
    }
}