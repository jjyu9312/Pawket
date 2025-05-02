package com.kkw.pawket.user.domain.repository

import com.kkw.pawket.user.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, String> {
    fun findByIdAndIsDeletedFalse(id: String): User?
    fun existsByIdAndIsDeletedFalse(id: String): Boolean
    fun existsByEmail(email: String): Boolean
}