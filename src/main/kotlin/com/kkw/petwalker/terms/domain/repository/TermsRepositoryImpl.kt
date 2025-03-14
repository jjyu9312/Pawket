package com.kkw.petwalker.terms.domain.repository

import com.kkw.petwalker.terms.domain.QTerms.terms
import com.kkw.petwalker.terms.domain.QUserTermsMapping.userTermsMapping
import com.kkw.petwalker.terms.domain.Terms
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class TermsRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory) : TermsRepositoryCustom {
    override fun findRequiredTermsNotAgreedByUser(userId: String): List<Terms> {
        return jpaQueryFactory
            .selectFrom(terms)
            .leftJoin(userTermsMapping)
            .on(terms.id.eq(userTermsMapping.terms.id)
                .and(userTermsMapping.user.id.eq(userId)))  // 유저와 약관 매핑된 경우 필터링
            .where(
                terms.isRequired.isTrue,  // 필수 약관만 선택
                userTermsMapping.terms.id.isNull  // 유저가 동의하지 않은 약관만 조회
            )
            .fetch()
    }
}