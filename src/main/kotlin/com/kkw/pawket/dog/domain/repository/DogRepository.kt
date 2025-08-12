package com.kkw.pawket.dog.domain.repository

import com.kkw.pawket.dog.domain.Dog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DogRepository : JpaRepository<Dog, String> {
    fun findByIdAndIsDeletedFalse(id: String): Dog?

    fun findAllByUserId(userId: String): List<Dog>
}