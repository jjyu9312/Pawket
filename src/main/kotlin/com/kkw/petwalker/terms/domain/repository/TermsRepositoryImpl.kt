package com.kkw.pawket.terms.domain.repository

import com.kkw.pawket.terms.domain.QTerms.terms
import com.kkw.pawket.terms.domain.QUserTermsMapping.userTermsMapping
import com.kkw.petwalker.terms.domain.Terms
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class TermsRepositoryImpl(private val jpaQueryFactory: JPAQueryFactory) : TermsRepositoryCustom {
    /*
        * 유저가 동의하지 않은 필수 약관을 조회한다.
     */
    override fun findRequiredTermsNotAgreedByUser(userId: String): List<Terms> {
        return jpaQueryFactory
            .selectFrom(terms)
            .leftJoin(userTermsMapping)
            .on(terms.id.eq(userTermsMapping.terms.id)
                .and(userTermsMapping.user.id.eq(userId)))
            .where(
                terms.isRequired.isTrue,
                userTermsMapping.terms.id.isNull
            )
            .fetch()
    }
}