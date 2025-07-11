package com.kkw.pawket.point.domain

enum class PointHistoryType(val stringValue: String) {
    COLLECT("collect"),
    USE("use"),
    ;

    companion object {
        fun fromString(value: String): PointHistoryType? {
            return entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}