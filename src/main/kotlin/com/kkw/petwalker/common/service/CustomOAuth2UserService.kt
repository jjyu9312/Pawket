package com.kkw.petwalker.common.service

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService
import org.springframework.security.oauth2.core.user.DefaultOAuth2User
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service

@Service
class CustomOAuth2UserService : DefaultOAuth2UserService() {

    // 역할: OAuth2 로그인 시 사용자 정보를 가져와서 처리하는 역할

    override fun loadUser(userRequest: OAuth2UserRequest): OAuth2User {
        val oAuth2User = super.loadUser(userRequest)

        // 사용자 정보 처리
        val attributes = oAuth2User.attributes
        val email = attributes["email"] as String
        val name = attributes["name"] as String

        return DefaultOAuth2User(oAuth2User.authorities, attributes, "email")
    }
}
