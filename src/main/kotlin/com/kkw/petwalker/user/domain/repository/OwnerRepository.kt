package com.kkw.petwalker.user.domain.repository

import com.kkw.petwalker.user.domain.Owner
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OwnerRepository: JpaRepository<Owner, String> {
}