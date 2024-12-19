package com.kkw.petwalker.user.domain.repository

import com.kkw.petwalker.user.domain.Walker
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalkerRepository: JpaRepository<Walker, String> {
}