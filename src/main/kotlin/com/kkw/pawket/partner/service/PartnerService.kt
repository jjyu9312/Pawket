package com.kkw.pawket.partner.service

import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.partner.domain.Partner
import com.kkw.pawket.partner.domain.repository.PartnerRepository
import com.kkw.pawket.partner.model.req.CreatePartnerReq
import com.kkw.pawket.partner.model.res.PartnerInfo
import com.kkw.pawket.partner.model.res.PartnerRegistrationStatusRes
import com.kkw.pawket.user.domain.repository.UserRepository
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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

    @Transactional(readOnly = true)
    fun checkPartnerRegistration(userId: String): PartnerRegistrationStatusRes {
        // 사용자 존재 여부 확인
        if (!userRepository.existsByIdAndIsDeletedFalse(userId)) {
            throw BadRequestException("사용자 정보를 찾을 수 없습니다.")
        }

        // 제휴업체 조회
        val partners = partnerRepository.findAllByUserIdAndIsDeletedFalse(userId)

        return if (partners.isNotEmpty()) {
            // 제휴업체로 등록된 경우
            PartnerRegistrationStatusRes(
                isRegistered = true,
                partners = partners.map { partner ->
                    PartnerInfo(
                        id = partner.id,
                        name = partner.name,
                        ownerName = partner.ownerName,
                        contactNumber = partner.contactNumber,
                        link = partner.link,
                    )

                }
            )
        } else {
            // 제휴업체로 등록되지 않은 경우
            PartnerRegistrationStatusRes(isRegistered = false, partners = emptyList())
        }
    }
}