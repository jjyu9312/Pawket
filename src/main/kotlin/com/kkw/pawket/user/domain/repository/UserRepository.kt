package com.kkw.pawket.user.domain.repository

import com.kkw.pawket.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun existsByEmail(email: String): Boolean
}