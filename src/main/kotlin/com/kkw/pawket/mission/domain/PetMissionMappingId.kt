package com.kkw.pawket.mission.domain

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class PetMissionMappingId(
    @Column(name = "pet_id", columnDefinition = "CHAR(36)")
    val petId: String,

    @Column(name = "mission_id", columnDefinition = "CHAR(36)")
    val missionId: String
) : Serializable
