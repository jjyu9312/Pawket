package com.kkw.pawket.notice.service

import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.notice.domain.Notice
import com.kkw.pawket.notice.domain.TargetGroup
import com.kkw.pawket.notice.domain.repository.NoticeRepository
import com.kkw.pawket.notice.model.req.CreateNoticeReq
import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository,
) {
    fun createNotice(req: CreateNoticeReq): String {
        val target = TargetGroup.fromString(req.target)
            ?: throw BadRequestException(ResponseCode.INVALID_TARGET_GROUP)

        if (target == TargetGroup.ALL) {
            // 모든 사용자에게 Firebase로 공지사항을 발송 알림
        } else if (target == TargetGroup.USER) {
            // 일반 사용자에게 Firebase로 공지사항을 발송 알림
        } else if (target == TargetGroup.PARTNER) {
            // 제휴업체에게 Firebase로 공지사항을 발송 알림
        } else if (target == TargetGroup.ADS_COMPANY) {
            // 광고업체에게 Firebase로 공지사항을 발송 알림
        } else {
            throw BadRequestException(ResponseCode.INVALID_TARGET_GROUP)
        }

        val notice = Notice.create(
            title = req.title,
            content = req.content,
            targetGroup = target,
            isRequired = req.isRequired,
            priority = req.priority,
        )

        noticeRepository.save(notice)

        return notice.id
    }
}