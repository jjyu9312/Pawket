package com.kkw.pawket.pet.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.pet.model.req.CreatePetReq
import com.kkw.pawket.pet.model.req.UpdatePetReq
import com.kkw.pawket.pet.service.PetService
import jakarta.validation.Valid
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/pets")
class PetController(private val petService: PetService) {
    /*
    TODO Pet 생성
     */
    @PostMapping
    fun createPet(
        @AuthenticationPrincipal userId: String,
        @Valid @RequestPart("petData") req: CreatePetReq,
        @RequestPart("petImages", required = false) petImages: List<MultipartFile>
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val petId = petService.createPet(userId, req, petImages)
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

    @PutMapping("/{petId}")
    fun updatePet(
        @AuthenticationPrincipal userId: String,
        @PathVariable petId: String,
        @Valid @RequestPart("petData") req: UpdatePetReq,
        @RequestPart("petImages", required = false) petImages: List<MultipartFile>?
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            petService.updatePet(userId, petId, req, petImages)
            ApiResponseFactory.success("Pet updated successfully")
        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,
                httpStatus = HttpStatus.BAD_REQUEST,
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }

    @DeleteMapping("/{petId}")
    fun deletePet(
        @AuthenticationPrincipal userId: String,
        @PathVariable petId: String
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            petService.deletePet(userId, petId)
            ApiResponseFactory.success("Pet deleted successfully")
        } catch (e: BadRequestException) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.BAD_REQUEST,
                httpStatus = HttpStatus.BAD_REQUEST,
                customMessage = e.message
            )
        } catch (e: Exception) {
            ApiResponseFactory.error(
                responseCode = ResponseCode.INTERNAL_SERVER_ERROR,
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
                customMessage = e.message
            )
        }
    }
}