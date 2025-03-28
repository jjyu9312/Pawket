package com.kkw.pawket.config

import jakarta.annotation.PostConstruct
import java.util.TimeZone
import org.springframework.stereotype.Component

@Component
class TimeZoneConfig {
    @PostConstruct
    fun init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"))
        println("Default time zone set to: " + TimeZone.getDefault().id)
    }
}
