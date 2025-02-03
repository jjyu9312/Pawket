package com.kkw.petwalker.reservation.domain.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RealWalkCourseRepository: JpaRepository<RealWalkCourse, String> {
}