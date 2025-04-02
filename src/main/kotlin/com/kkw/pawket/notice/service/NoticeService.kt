package com.kkw.pawket.notice.service

import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.notice.domain.TargetGroup
import com.kkw.pawket.notice.domain.repository.NoticeRepository
import com.kkw.pawket.notice.model.req.CreateNoticeReq
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository,
) {
    fun createNotice(req: CreateNoticeReq): String? {
        val target = TargetGroup.fromString(req.target)
            ?: throw BadRequestException(
                ResponseCode.INVALID_TARGET_GROUP.defaultMessage
            )

        if (target == TargetGroup.ALL) {
            // 모든 사용자에게 Firebase로 공지사항을 발송 알림
        } else if (target == TargetGroup.USER) {
            // 일반 사용자에게 Firebase로 공지사항을 발송 알림
        } else if (target == TargetGroup.PARTNER) {
            // 제휴업체에게 Firebase로 공지사항을 발송 알림
        } else if (target == TargetGroup.ADS_COMPANY) {
            // 광고업체에게 Firebase로 공지사항을 발송 알림
        }

        val notice = noticeRepository.save(
            com.kkw.pawket.notice.domain.Notice(
                title = req.title,
                content = req.content,
                targetGroup = target,
                isRequired = req.isRequired,
                priority = req.priority
            )
        )
        return notice.id
    }
}