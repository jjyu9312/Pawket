package com.kkw.pawket.checklist.domain

import com.kkw.pawket.common.domain.BaseEntity
import com.kkw.pawket.pet.domain.Pet
import jakarta.persistence.*
import java.time.LocalDate
import java.util.*

@Entity(name = "checklist")
data class Checklist(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @JoinColumn(name = "pet_id", columnDefinition = "CHAR(36)")
    @ManyToOne(fetch = FetchType.LAZY)
    val pet: Pet,

    @JoinColumn(name = "check_type_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    val checkType: CheckType,

    @Column(nullable = false)
    val checkDate: LocalDate = LocalDate.now(),

    @Column(nullable = false)
    var isChecked: Boolean = false,

    ) : BaseEntity() {
    constructor(
        pet: Pet,
        checkType: CheckType,
        isChecked: Boolean,
    ) : this(
        id = UUID.randomUUID().toString(),
        pet = pet,
        checkType = checkType,
        isChecked = isChecked,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Checklist
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()
}
