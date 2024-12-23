package com.kkw.petwalker.reservation.domain.repository

import com.kkw.petwalker.reservation.domain.WalkCourse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalkCourseRepository: JpaRepository<WalkCourse, String> {
}