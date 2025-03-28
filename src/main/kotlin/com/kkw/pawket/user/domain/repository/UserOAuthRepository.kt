package com.kkw.pawket.user.domain.repository

import com.kkw.pawket.user.domain.OAuthProvider
import com.kkw.pawket.user.domain.UserOAuth
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserOAuthRepository : JpaRepository<UserOAuth, String> {
    fun findByProviderAndProviderUserId(
        provider: OAuthProvider,
        providerUserId: String
    ): UserOAuth?
}