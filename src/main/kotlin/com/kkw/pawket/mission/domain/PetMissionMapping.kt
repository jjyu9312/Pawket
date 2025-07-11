package com.kkw.pawket.mission.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.pet.domain.Pet
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "pet_mission_mapping")
data class PetMissionMapping(
    @EmbeddedId
    val id: PetMissionMappingId,

    @MapsId("petId")
    @JoinColumn(name = "pet_id", columnDefinition = "CHAR(36)")
    @ManyToOne(fetch = FetchType.LAZY)
    val pet: Pet,

    @MapsId("missionId")
    @JoinColumn(name = "check_type_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val mission: Mission,

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
        other as PetMissionMapping
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            pet: Pet,
            mission: Mission,
        ): PetMissionMapping {
            return PetMissionMapping(
                id = PetMissionMappingId(pet.id, mission.id),
                pet = pet,
                mission = mission,
                isSucceed = false,
                startedAt = LocalDateTime.now(),
                finishedAt = null
            )
        }
    }
}
