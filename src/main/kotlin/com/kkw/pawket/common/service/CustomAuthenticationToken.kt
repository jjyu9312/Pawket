package com.kkw.pawket.common.service

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class CustomAuthenticationToken(
    private val principal: String,
    private val authorities: Collection<GrantedAuthority> = emptyList()
) : AbstractAuthenticationToken(authorities) {
    // 역할: Spring Security에서 사용자의 인증 정보를 나타내는 객체
    // -> Spring Security의 SecurityContext에 저장되어 요청이 진행되는 동안 사용자와 관련된 인증 상태 및 권한 정보를 제공

    init {
        // 인증 상태를 true로 설정
        isAuthenticated = true
    }

    override fun getPrincipal(): String = principal

    override fun getCredentials(): Any? = null // JWT를 사용하므로 인증 자격 정보는 필요 없음
}
