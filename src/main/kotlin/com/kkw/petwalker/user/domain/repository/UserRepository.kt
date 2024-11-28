package com.kkw.petwalker.user.domain.repository

import com.kkw.petwalker.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, String> {
}