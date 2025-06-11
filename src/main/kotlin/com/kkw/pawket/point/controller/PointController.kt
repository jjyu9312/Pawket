package com.kkw.pawket.point.controller

import com.kkw.pawket.common.response.ApiResponse
import com.kkw.pawket.common.response.ApiResponseFactory
import com.kkw.pawket.common.response.ResponseCode
import com.kkw.pawket.point.model.req.CreatePointReq
import com.kkw.pawket.point.model.req.CreatePointShopItemReq
import com.kkw.pawket.point.model.req.CreatePointShopReq
import com.kkw.pawket.point.model.res.ReadPointShopRes
import com.kkw.pawket.point.service.PointService
import com.kkw.pawket.point.service.PointShopService
import io.swagger.v3.oas.annotations.Operation
import org.apache.coyote.BadRequestException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/point")
class PointController(
    private val pointService: PointService,
    private val pointShopService: PointShopService,
) {
    /*
    TODO 보상 등록
     */
    @Operation(
        summary = "보상 등록",
        description = "보상을 등록합니다."
    )
    @PostMapping
    fun createPoint(
        @AuthenticationPrincipal userId: String,
        @RequestBody req: CreatePointReq
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val partnerId = pointService.createPoint(userId, req)
            ApiResponseFactory.success(partnerId)
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
    TODO 포인트샵 등록
     */
    @Operation(
        summary = "포인트샵 등록",
        description = "포인트샵을 등록합니다."
    )
    @PostMapping("/shop")
    fun createPointShop(
        @AuthenticationPrincipal userId: String,
        @RequestBody req: CreatePointShopReq
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val pointShopId = pointShopService.createPointShop(userId, req)
            ApiResponseFactory.success(pointShopId)
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

    /**
     * TODO 포인트샵 제품 등록
     */
    @Operation(
        summary = "포인트샵 제품 등록",
        description = "포인트샵 제품을 등록합니다."
    )
    @PostMapping("/shop/item")
    fun createPointShopItem(
        @AuthenticationPrincipal userId: String,
        @RequestBody req: CreatePointShopItemReq
    ): ResponseEntity<ApiResponse<String>> {
        return try {
            val pointShopItemId = pointShopService.createPointShopItem(userId, req)
            ApiResponseFactory.success(pointShopItemId)
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
    TODO 유저가 등록한 포인트샵 조회
     */
    @Operation(
        summary = "유저가 등록한 포인트샵 조회",
        description = "유저가 등록한 포인트샵 목록을 조회합니다."
    )
    @GetMapping("/shops/byUser")
    fun getPointShopsByUser(
        @AuthenticationPrincipal userId: String
    ): ResponseEntity<ApiResponse<List<ReadPointShopRes>>> {
        return try {
            val pointShops = pointShopService.getPointShopsByUser(userId)
            ApiResponseFactory.success(pointShops)
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
    TODO 특정 포인트샵 제품 목록 조회
     */
}