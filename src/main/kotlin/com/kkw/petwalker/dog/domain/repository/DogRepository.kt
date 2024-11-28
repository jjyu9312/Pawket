package com.kkw.petwalker.dog.domain.repository

import com.kkw.petwalker.dog.domain.Dog
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DogRepository: JpaRepository<Dog, String> {
}