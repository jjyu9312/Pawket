package com.kkw.petwalker.notice.domain

enum class TargetGroup(val stringValue: String) {
    ALL("all"),
    OWNER("owner"),
    WALKER("walker")
    ;

    companion object {
        fun fromString(value: String): TargetGroup? {
            return TargetGroup.entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}
