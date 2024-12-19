package com.kkw.petwalker.user.domain

enum class Gender(val stringValue: String) {
    MALE("male"),
    FEMALE("female");

    companion object {
        fun fromString(value: String): Gender? {
            return entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}