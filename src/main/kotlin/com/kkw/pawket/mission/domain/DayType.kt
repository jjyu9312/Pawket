package com.kkw.pawket.mission.domain

enum class DayType(val stringValue: String) {
    daily("daily"),
    weekly("weekly"),
    monthly("monthly");
    ;

    companion object {
        fun fromString(value: String): DayType? {
            return entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}
