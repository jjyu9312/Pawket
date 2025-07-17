package com.kkw.pawket.mission.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.pet.domain.Pet
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "pet_mission_history")
data class PetMissionHistory(
    @EmbeddedId
    val id: PetMissionHistoryId,

    @MapsId("petId")
    @JoinColumn(name = "pet_id", columnDefinition = "CHAR(36)")
    @ManyToOne(fetch = FetchType.LAZY)
    val pet: Pet,

    @MapsId("missionId")
    @JoinColumn(name = "mission_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val mission: Mission,

    @Column(nullable = false)
    var isSucceed: Boolean = false,

    @Column(nullable = true, length = 1000)
    var notes: String? = null,

    @Column(nullable = false)
    var startedAt: LocalDateTime,

    @Column(nullable = true)
    var finishedAt: LocalDateTime? = null,

    ) : BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as PetMissionHistory
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            pet: Pet,
            mission: Mission,
            notes: String?,
        ): PetMissionHistory {
            return PetMissionHistory(
                id = PetMissionHistoryId(pet.id, mission.id),
                pet = pet,
                mission = mission,
                isSucceed = false,
                notes = notes,
                startedAt = LocalDateTime.now(),
                finishedAt = null
            )
        }
    }
}
