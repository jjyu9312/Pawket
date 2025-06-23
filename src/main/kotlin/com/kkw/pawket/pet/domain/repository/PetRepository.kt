package com.kkw.pawket.pet.domain.repository

import com.kkw.pawket.pet.domain.Pet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PetRepository : JpaRepository<Pet, String> {
    fun findByIdAndIsDeletedFalse(id: String): Pet?

    fun findAllByUserId(userId: String): List<Pet>
}