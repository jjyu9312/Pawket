package com.kkw.pawket.ai.service

import com.kkw.pawket.config.OssMessage
import com.kkw.pawket.config.OssRequest
import com.kkw.pawket.config.OssResponse
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class DogAgentService(
    private val webClient: WebClient,
    private val tools: ToolRegistry
) {

    fun chat(userMsg: String): String {
        var messages = mutableListOf(
            OssMessage("system", "You are Pawket's pet-care agent. Be concise; ask at most one follow-up."),
            OssMessage("user", userMsg)
        )

        repeat(3) { // 간단한 루프 (툴 최대 3회)
            val res = callModel(messages)
            val toolCalls = extractToolCalls(res)
            if (toolCalls.isEmpty()) return extractText(res)

            val toolResults = toolCalls.map { call ->
                val result = tools.invoke(call.name, call.arguments)
                OssMessage("tool", """{"tool":"${call.name}","result":${result}}""")
            }
            messages.addAll(toolResults)
        }
        return "죄송해요, 요청을 마무리하지 못했어요. 조금 더 구체적으로 알려주실 수 있을까요?"
    }

    private fun callModel(messages: List<OssMessage>): OssResponse =
        webClient.post()
            .uri("/responses") // Workers AI/자체 호환 엔드포인트 예시
            .bodyValue(
                OssRequest(
                model = System.getenv("OSS_MODEL") ?: "gpt-oss-20b",
                messages = messages,
                tools = ToolSchemas.definitions()
            )
            )
            .retrieve()
            .bodyToMono(OssResponse::class.java)
            .block()!!

    private fun extractToolCalls(res: OssResponse): List<OssToolCall> =
        res.tool_calls ?: emptyList()

    private fun extractText(res: OssResponse): String =
        res.content ?: (res.output?.get("text") as? String ?: "")
}