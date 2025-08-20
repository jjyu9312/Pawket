package com.kkw.pawket.ai.service

object ToolSchemas {
    fun definitions(): List<Map<String, Any>> = listOf(
        mapOf(
            "type" to "function",
            "function" to mapOf(
                "name" to "findVetClinics",
                "description" to "도시 기준 동물병원 검색",
                "parameters" to mapOf(
                    "type" to "object",
                    "properties" to mapOf("city" to mapOf("type" to "string")),
                    "required" to listOf("city")
                )
            )
        )
        // ... 다른 툴들 계속 추가
    )
}