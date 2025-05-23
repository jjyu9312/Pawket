package com.kkw.pawket.point.domain

enum class PointType(val stringValue: String) {
    WALK("walk"),
    AD("ad"),
    FEED("feed"),
    ETC("etc"),
    ;

    companion object {
        fun fromString(value: String): PointType? {
            return entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}
