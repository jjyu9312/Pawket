package com.kkw.petwalker.terms.model.res

data class RequiredTermsAgreeCheckRes(
    val isAgreed: Boolean,
    val notAgreedRequiredTerms: List<String>? = null,
)