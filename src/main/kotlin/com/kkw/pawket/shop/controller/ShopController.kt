package com.kkw.pawket.shop.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.partner.model.req.CreatePartnerReq
import com.kkw.pawket.shop.model.req.CreateShopReq
import com.kkw.pawket.shop.model.req.UpdateShopReq
import com.kkw.pawket.shop.service.ShopService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/shops")
class ShopController(
    private val shopService: ShopService
) {
    /*
    TODO Shop Company 생성
     */
    @Operation(
        summary = "Shop Company 생성",
        description = "새로운 Shop Company를 생성합니다."
    )
    @PostMapping
    fun createShop(
        @AuthenticationPrincipal userId: String,
        @RequestBody req: CreateShopReq
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val shopId = shopService.createShop(userId, req)
            ApiResponseFactory.success(shopId)
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

    /*
    TODO Shop Company 조회
     */

    /*
    TODO Shop Company 수정
     */
    @PutMapping("{shopId}")
    fun updateShop(
        @AuthenticationPrincipal userId: String,
        @PathVariable shopId: String,
        @RequestBody req: UpdateShopReq,
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            shopService.updateShop(userId, shopId, req)
            ApiResponseFactory.success("Shop updated successfully")
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

    /*
    TODO Shop Company 삭제
     */
    @DeleteMapping("{shopId}")
    fun deleteShop(
        @AuthenticationPrincipal userId: String,
        @PathVariable shopId: String
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            shopService.deleteShop(userId, shopId)
            ApiResponseFactory.success("Shop deleted successfully")
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