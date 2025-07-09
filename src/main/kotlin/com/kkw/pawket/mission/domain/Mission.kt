package com.kkw.pawket.mission.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.pet.domain.Pet
import jakarta.persistence.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Entity(name = "mission")
data class Mission(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @JoinColumn(name = "pet_id", columnDefinition = "CHAR(36)")
    @ManyToOne(fetch = FetchType.LAZY)
    val pet: Pet,

    @JoinColumn(name = "check_type_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val missionType: MissionType,

    @Column(nullable = false)
    var isSucceed: Boolean = false,

    @Column(nullable = false)
    var startedAt: LocalDateTime,

    @Column(nullable = true)
    var finishedAt: LocalDateTime? = null,

    ) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Mission
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            pet: Pet,
            missionType: MissionType,
            isSucceed: Boolean = false,
            startedAt: LocalDateTime = LocalDateTime.now(),
            finishedAt: LocalDateTime? = null
        ): Mission = Mission(
            pet = pet,
            missionType = missionType,
            isSucceed = isSucceed,
            startedAt = startedAt,
            finishedAt = finishedAt
        )
    }
}
