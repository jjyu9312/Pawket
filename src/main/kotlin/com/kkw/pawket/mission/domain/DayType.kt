package com.kkw.pawket.mission.domain

enum class DayType(val stringValue: String) {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly");
    ;

    companion object {
        fun fromString(value: String): DayType? {
            return entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}
