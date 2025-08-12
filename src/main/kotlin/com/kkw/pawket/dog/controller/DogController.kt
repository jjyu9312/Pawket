package com.kkw.pawket.dog.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.dog.model.req.CreateDogReq
import com.kkw.pawket.dog.model.req.UpdateDogReq
import com.kkw.pawket.dog.service.DogService
import jakarta.validation.Valid
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/dogs")
class DogController(private val dogService: DogService) {
    /*
    TODO dog 생성
     */
    @PostMapping
    fun createDog(
        @AuthenticationPrincipal userId: String,
        @Valid @RequestPart("dogData") req: CreateDogReq,
        @RequestPart("dogImages", required = false) dogImages: List<MultipartFile>
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val dogId = dogService.createDog(userId, req, dogImages)
            ApiResponseFactory.success(dogId)
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

    @PutMapping("/{dogId}")
    fun updateDog(
        @AuthenticationPrincipal userId: String,
        @PathVariable dogId: String,
        @Valid @RequestPart("dogData") req: UpdateDogReq,
        @RequestPart("dogImages", required = false) dogImages: List<MultipartFile>?
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            dogService.updateDog(userId, dogId, req, dogImages)
            ApiResponseFactory.success("dog updated successfully")
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

    @DeleteMapping("/{dogId}")
    fun deleteDog(
        @AuthenticationPrincipal userId: String,
        @PathVariable dogId: String
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            dogService.deleteDog(userId, dogId)
            ApiResponseFactory.success("dog deleted successfully")
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