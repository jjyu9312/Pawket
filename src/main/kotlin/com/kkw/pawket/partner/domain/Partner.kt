package com.kkw.pawket.partner.domain

import com.kkw.pawket.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity(name = "animal_hospital")
data class Partner(
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    val ownerName: String,

    @Column(nullable = false, length = 50)
    val contactNumber: String,

    @Column(nullable = true)
    val link: String,

    ) : BaseEntity() {

    constructor(
        name: String,
        ownerName: String,
        contactNumber: String,
        link: String,
    ) : this(
        id = UUID.randomUUID().toString(),
        name = name,
        ownerName = ownerName,
        contactNumber = contactNumber,
        link = link,
    )
}