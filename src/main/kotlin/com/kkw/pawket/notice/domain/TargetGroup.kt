package com.kkw.pawket.notice.domain

enum class TargetGroup(val stringValue: String) {
    ALL("all"),
    USER("user"),
    PARTNER("partner"),
    ADS_COMPANY("ads_company"),
    ;

    companion object {
        fun fromString(value: String): TargetGroup? {
            return TargetGroup.entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}
