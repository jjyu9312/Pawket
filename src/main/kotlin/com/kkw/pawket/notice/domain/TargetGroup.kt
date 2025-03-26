package com.kkw.pawket.notice.domain

enum class TargetGroup(val stringValue: String) {
    ALL("all"),
    USER("user"),
    COMPANY("company"),
    HOSPITAL("hospital"),
    ;

    companion object {
        fun fromString(value: String): TargetGroup? {
            return TargetGroup.entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}
