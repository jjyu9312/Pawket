package com.kkw.pawket.point.domain

enum class PointType(val stringValue: String) {
    WALK("walk"),
    AD("ad"),
    FEED("feed"),
    POINT_SHOP_ITEM("point_shop_item"),
    COUPON("coupon"),
    ETC("etc"),
    ;

    companion object {
        fun fromString(value: String): PointType? {
            return entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}
