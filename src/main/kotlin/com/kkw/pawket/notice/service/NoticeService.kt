package com.kkw.pawket.notice.service

import com.kkw.pawket.common.exception.BadRequestException
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.notice.domain.Notice
import com.kkw.pawket.notice.domain.TargetGroup
import com.kkw.pawket.notice.domain.repository.NoticeRepository
import com.kkw.pawket.notice.model.req.CreateNoticeReq
import com.kkw.pawket.notice.model.res.NoticeDetailRes
import com.kkw.pawket.user.domain.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class NoticeService(
    private val noticeRepository: NoticeRepository,
    private val userRepository: UserRepository,
) {
    fun createNotice(userId: String, req: CreateNoticeReq): String {
        userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

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

    fun updateNotice(userId: String, noticeId: String, req: CreateNoticeReq): String {
        userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val notice = noticeRepository.findByIdAndIsDeletedFalse(noticeId)
            ?: throw BadRequestException(ResponseCode.NOTICE_NOT_FOUND)

        val updateNotice = Notice.update(
            notice = notice,
            title = req.title,
            content = req.content,
            isRequired = req.isRequired,
            priority = req.priority,
        )

        noticeRepository.save(updateNotice)

        return updateNotice.id
    }

    fun deleteNotice(userId: String, noticeId: String) {
        userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val notice = noticeRepository.findByIdAndIsDeletedFalse(noticeId)
            ?: throw BadRequestException(ResponseCode.NOTICE_NOT_FOUND)

        notice.isDeleted = true
        noticeRepository.save(notice)
    }

    fun findNoticeById(userId: String, noticeId: String): NoticeDetailRes {
        userRepository.findByIdAndIsDeletedFalse(userId)
            ?: throw BadRequestException(ResponseCode.USER_NOT_FOUND)

        val notice = noticeRepository.findByIdAndIsDeletedFalse(noticeId)
            ?: throw BadRequestException(ResponseCode.NOTICE_NOT_FOUND)

        return NoticeDetailRes(
            id = notice.id,
            title = notice.title,
            content = notice.content,
            targetGroup = notice.targetGroup.toString(),
            isRequired = notice.isRequired,
            priority = notice.priority
        )
    }
}