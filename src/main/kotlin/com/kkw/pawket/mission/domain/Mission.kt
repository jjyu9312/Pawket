package com.kkw.pawket.mission.domain

import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity(name = "check_type")
data class Mission(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false, unique = true)
    val name: String,

    @Column(nullable = false)
    val content: String? = null,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val dayType: DayType = DayType.DAILY

) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Mission
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        private val regex = Regex("^[A-Za-z]+$")

        fun create(
            name: String,
            content: String? = null,
            dayType: DayType = DayType.DAILY
        ): Mission {
            require(name.isNotBlank()) { "Name must not be blank" }
            require(name.length <= 50) { "Name must be less than or equal to 50 characters" }

            return Mission(
                name = name,
                content = content,
                dayType = dayType
            )
        }

    }
}
