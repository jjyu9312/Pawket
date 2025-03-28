package com.kkw.pawket.spot.domain

enum class ImportanceLevel(val stringValue: String) {
    HIGH("high"),
    MEDIUM("medium"),
    LOW("low");

    companion object {
        fun fromString(value: String): ImportanceLevel? {
            return entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}