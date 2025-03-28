package com.kkw.pawket.user.domain

enum class OAuthProvider(val stringValue: String) {
    GOOGLE("google"),
    KAKAO("kakao"),
    APPLE("apple"),
    ;

    companion object {
        fun fromString(value: String): OAuthProvider? {
            return OAuthProvider.entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}