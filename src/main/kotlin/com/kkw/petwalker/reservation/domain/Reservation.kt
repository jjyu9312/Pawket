package com.kkw.petwalker.reservation.domain

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity(name = "reservation")
data class Reservation(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    val directorName: String,

    @Column(nullable = false)
    val contactNumber: String,

    ) : BaseEntity() {

    constructor(
        name: String,
        directorName: String,
        contactNumber: String,
    ) : this(
        id = UUID.randomUUID().toString(),
        name = name,
        directorName = directorName,
        contactNumber = contactNumber,
    )
}