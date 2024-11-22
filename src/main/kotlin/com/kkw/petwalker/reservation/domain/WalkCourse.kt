package com.kkw.petwalker.reservation.domain

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class WalkCourse (
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", columnDefinition = "CHAR(36)")
    val reservation: Reservation,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val walkCourseLatList: String,

    @Column(nullable = false)
    val walkCourseLngList: String,

): BaseEntity()