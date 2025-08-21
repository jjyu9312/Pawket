package com.kkw.pawket.mission.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.dog.domain.Dog
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "pet_mission_history")
data class DogMissionHistory(
    @EmbeddedId
    val id: DogMissionHistoryId,

    @MapsId("petId")
    @JoinColumn(name = "dog_id", columnDefinition = "CHAR(36)")
    @ManyToOne(fetch = FetchType.LAZY)
    val dog: Dog,

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
        other as DogMissionHistory
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    companion object {
        fun create(
            dog: Dog,
            mission: Mission,
            notes: String?,
        ): DogMissionHistory {
            return DogMissionHistory(
                id = DogMissionHistoryId(dog.id, mission.id),
                dog = dog,
                mission = mission,
                isSucceed = false,
                notes = notes,
                startedAt = LocalDateTime.now(),
                finishedAt = null
            )
        }
    }
}
