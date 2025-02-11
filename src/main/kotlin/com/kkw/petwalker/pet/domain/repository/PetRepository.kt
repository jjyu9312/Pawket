package com.kkw.petwalker.pet.domain.repository

import com.kkw.petwalker.pet.domain.Pet
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PetRepository: JpaRepository<Pet, String> {
}