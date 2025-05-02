package com.kkw.pawket.spot.model.req

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Request object for creating a new spot", name = "CreateSpotReq")
data class CreateSpotReq(
    @Schema(description = "Name of the spot", example = "Central Park")
    val name: String,

    @Schema(description = "Detailed description of the spot", example = "A large public park in the city center with walking trails and playgrounds.")
    val detail: String,

    @Schema(description = "Importance level of the spot (e.g., HIGH, MEDIUM, LOW)", example = "HIGH")
    val importanceLevel: String,

    @Schema(description = "Basic address of the spot", example = "123 Main St, Seoul")
    val addressBasic: String,

    @Schema(description = "Latitude of the spot", example = "37.5665")
    val addressLat: Double,

    @Schema(description = "Longitude of the spot", example = "126.9780")
    val addressLng: Double,

    @Schema(description = "Detailed address of the spot (optional)", example = "Near the main entrance, next to the fountain")
    val addressDetail: String? = null,
)
