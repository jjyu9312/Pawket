package com.kkw.pawket.reservation.domain

enum class ReservationStatus(val stringValue: String) {
    REQUESTED("requested"), // 사용자가 예약 요청
    CONFIRMED("confirmed"), // 파트너가 예약 수락
    CANCELED("canceled"), // 사용자가 취소
    REJECTED("rejected"), // 파트너가 거절
    COMPLETED("completed"), // 예약 완료
    NO_SHOW("no_show"), // 사용자가 예약 후 방문하지 않음
    ;

    companion object {
        fun fromString(value: String): ReservationStatus? {
            return entries.find { it.stringValue.equals(value, ignoreCase = true) }
        }
    }
}
