package com.kkw.pawket.pet.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.pet.model.req.CreatePetReq
import com.kkw.pawket.pet.service.PetService
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/pet")
class PetController(private val petService: PetService) {
    /*
    TODO Pet 생성
     */
    @PostMapping("/user/{userId}")
    fun createPet(
        @AuthenticationPrincipal userId: String,
        @RequestBody req: CreatePetReq,
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val petId = petService.createPet(userId, req)
            ApiResponseFactory.success(petId)
        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,  // 400 응답 코드
                httpStatus = HttpStatus.BAD_REQUEST,
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,  // 500 응답 코드
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }


}