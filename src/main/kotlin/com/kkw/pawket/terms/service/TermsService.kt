package com.kkw.pawket.terms.service

import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.terms.domain.Terms
import com.kkw.pawket.terms.domain.UserTermsMapping
import com.kkw.pawket.terms.domain.repository.TermsRepository
import com.kkw.pawket.terms.domain.repository.UserTermsMappingRepository
import com.kkw.pawket.terms.model.req.TermsCreateReq
import com.kkw.pawket.terms.model.res.RequiredTermsAgreeCheckRes
import com.kkw.pawket.terms.model.res.TermsListRes
import com.kkw.pawket.user.domain.repository.UserRepository
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service

@Service
class TermsService(
    private val termsRepository: TermsRepository,
    private val userRepository: UserRepository,
    private val userTermsMappingRepository: UserTermsMappingRepository,
) {
    fun createTerms(termsCreateReq: TermsCreateReq): String {
        val terms = Terms(
            title = termsCreateReq.title,
            content = termsCreateReq.content,
            isRequired = termsCreateReq.isRequired,
        )

        termsRepository.save(terms)

        return terms.id
    }

    fun getTerms(): List<TermsListRes> {
        val termsList = termsRepository.findAll()

        return termsList.map {
            TermsListRes(
                id = it.id,
                name = it.title,
                url = it.content,
                isRequired = it.isRequired,
            )
        }
    }

    fun agreeToTerms(userId: String, agreeTermsList: List<String>) {
        val user = userRepository.findById(userId).orElseThrow {
            throw BadRequestException(
                ResponseCode.USER_NOT_FOUND.defaultMessage
            )
        }

        val termsList = termsRepository.findAllById(agreeTermsList)

        val alreadyAgreedTermsIdList = userTermsMappingRepository.findAllByUserIdAndTermsIdIn(userId, termsList.map { it.id })
            .map { it.terms.id }

        val duplicatedTerms = termsList.filter { alreadyAgreedTermsIdList.contains(it.id) }
        if (duplicatedTerms.isNotEmpty()) {
            throw BadRequestException(
                ResponseCode.ALREADY_AGREED_TERMS.defaultMessage
            )
        }

        val newAgreeUserTermsMappingList = termsList.map { term ->
            UserTermsMapping(
                user = user,
                terms = term,
                isAgreed = true,
            )
        }

        userTermsMappingRepository.saveAll(newAgreeUserTermsMappingList)
    }

    fun checkAgreedTerms(userId: String): RequiredTermsAgreeCheckRes? {
        userRepository.findById(userId).orElseThrow {
            throw BadRequestException(
                ResponseCode.USER_NOT_FOUND.defaultMessage
            )
        }

        val notAgreedTermsList = termsRepository.findRequiredTermsNotAgreedByUser(userId)

        return if (notAgreedTermsList.isNotEmpty()) {
            RequiredTermsAgreeCheckRes(
                isAgreed = false,
                notAgreedRequiredTerms = notAgreedTermsList.map {
                    it.id
                }
            )
        } else {
            RequiredTermsAgreeCheckRes(isAgreed = true, null)
        }
    }
}