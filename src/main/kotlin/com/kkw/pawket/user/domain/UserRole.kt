package com.kkw.pawket.user.domain

enum class UserRole(val stringValue: String) {
    ADMIN("Admin"),
    USER("User");

    companion object {
        fun fromString(value: String): UserRole? {
            return entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}