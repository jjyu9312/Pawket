package com.kkw.petwalker.reservation.domain

import com.kkw.petwalker.common.domain.BaseEntity
import jakarta.persistence.*
import java.util.*

@Entity
data class RealWalkCourse (
    @Id
    @Column(nullable = false, columnDefinition = "CHAR(36)")
    val id: String = UUID.randomUUID().toString(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", columnDefinition = "CHAR(36)")
    val reservation: Reservation,

    @Column(nullable = false, length = 50)
    val name: String,

    @Column(nullable = false) // ,로 연결 (순서 중요)
    val realWalkCourseLatList: String,

    @Column(nullable = false) // ,로 연결 (순서 중요)
    val realWalkCourseLngList: String,

): BaseEntity()