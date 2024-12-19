package com.kkw.petwalker.dog.domain

enum class Sex(val stringValue: String) {
    MALE("male"),
    FEMALE("female");

    companion object {
        fun fromString(value: String): Sex? {
            return values().find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}
