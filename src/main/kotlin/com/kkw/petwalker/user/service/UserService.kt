package com.kkw.petwalker.user.service

import com.kkw.petwalker.dog.domain.Dog
import com.kkw.petwalker.dog.domain.repository.DogRepository
import com.kkw.petwalker.user.domain.User
import com.kkw.petwalker.user.domain.repository.UserRepository
import com.kkw.petwalker.user.dto.CreateUserDto
import org.springframework.stereotype.Service

@Service
class UserService (
    private val userRepository: UserRepository,
    private val dogRepository: DogRepository,
) {
    fun createUser(req: CreateUserDto.Req): String {
        val user = User(
            name = req.name,
            birth = req.birth,
            email = req.email,
            addressBasic = req.addressInfo.basic,
            addressLat = req.addressInfo.lat,
            addressLng = req.addressInfo.lng,
            addressDetail = req.addressInfo.detail,
        )


        val dog = Dog(
            owner = User,

        )

        userRepository.save(user)

        return user.id
    }

    fun login(): String {
        TODO("Not yet implemented")
    }

    fun logout(): Any {
        TODO("Not yet implemented")
    }

}