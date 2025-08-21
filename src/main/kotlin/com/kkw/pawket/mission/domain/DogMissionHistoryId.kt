package com.kkw.pawket.mission.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class DogMissionHistoryId(
    @Column(name = "dog_id", columnDefinition = "CHAR(36)")
    val dogId: String,

    @Column(name = "mission_id", columnDefinition = "CHAR(36)")
    val missionId: String
) : Serializable
