package com.kkw.pawket.ai.service

import com.kkw.pawket.ai.config.ClovaConfig
import com.kkw.pawket.ai.model.common.ChatMessage
import com.kkw.pawket.ai.model.common.PetProfile
import com.kkw.pawket.ai.model.common.TokenUsage
import com.kkw.pawket.ai.model.req.OpenAIReq
import com.kkw.pawket.ai.model.res.OpenAIRes
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBody

@Service
class ClovaAgentService(
    @Qualifier("clovaWebClient") private val webClient: WebClient,
    private val clovaConfig: ClovaConfig
) {

    private val logger = LoggerFactory.getLogger(ClovaAgentService::class.java)

    suspend fun generateResponse(
        userMessage: String,
        context: List<ChatMessage> = emptyList(),
        petProfile: PetProfile? = null
    ): Pair<String, TokenUsage?> {

        val systemPrompt = createPetCareSystemPrompt(petProfile)
        val messages = mutableListOf<ChatMessage>().apply {
            add(ChatMessage("system", systemPrompt))
            addAll(context.takeLast(8)) // 최근 8개 메시지만 유지
            add(ChatMessage("user", userMessage))
        }

        return try {
            // CLOVA API 호출 (재시도 로직 포함)
            val result = callClovaWithRetry(messages)
            Pair(result.first, result.second)

        } catch (e: Exception) {
            logger.error("CLOVA API 호출 실패", e)
            Pair("죄송합니다. 일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", null)
        }
    }

    private suspend fun callClovaWithRetry(
        messages: List<ChatMessage>,
        retryCount: Int = 0
    ): Pair<String, TokenUsage?> {
        return try {
            callClovaAPI(messages)
        } catch (e: Exception) {
            if (retryCount < clovaConfig.maxRetries) {
                logger.warn("CLOVA API 호출 실패, 재시도 ${retryCount + 1}/${clovaConfig.maxRetries}", e)
                delay(1000L * (retryCount + 1)) // 지수 백오프
                callClovaWithRetry(messages, retryCount + 1)
            } else {
                throw e
            }
        }
    }

    private suspend fun callClovaAPI(messages: List<ChatMessage>): Pair<String, TokenUsage?> {
        // OpenAI 호환 API 사용
        val request = OpenAIReq(
            model = clovaConfig.defaultModel,
            messages = messages,
            temperature = 0.7,
            maxTokens = 512
        )

        val response = webClient
            .post()
            .uri("/chat/completions")
            .bodyValue(request)
            .retrieve()
            .awaitBody<OpenAIRes>()

        val reply = response.choices.firstOrNull()?.message?.content
            ?: throw RuntimeException("응답에서 메시지를 찾을 수 없습니다")

        val tokenUsage = response.usage?.let {
            TokenUsage(
                inputTokens = it.promptTokens,
                outputTokens = it.completionTokens,
                totalTokens = it.totalTokens
            )
        }

        return Pair(reply, tokenUsage)
    }

    private fun createPetCareSystemPrompt(petProfile: PetProfile?): String {
        val basePrompt = """
            당신은 전문적이고 친근한 반려동물 상담사입니다.
            
            역할:
            - 반려동물의 건강, 행동, 훈련에 대한 전문적인 조언 제공
            - 응급상황 시 즉시 병원 방문이 필요한지 판단
            - 친근하고 따뜻한 톤으로 대화
            
            주의사항:
            - 확실하지 않은 의학적 진단은 하지 않기
            - 심각한 증상의 경우 반드시 수의사 상담 권유
            - 구체적이고 실용적인 조언 제공
            - 응답은 한국어로 작성
        """.trimIndent()

        return if (petProfile != null) {
            basePrompt + """
            
            현재 상담 중인 반려동물 정보:
            - 이름: ${petProfile.name}
            - 종류: ${petProfile.species}
            - 품종: ${petProfile.breed}
            - 나이: ${petProfile.age}세
            ${if (petProfile.weight != null) "- 몸무게: ${petProfile.weight}kg" else ""}
            ${if (petProfile.healthConditions.isNotEmpty()) "- 건강상태: ${petProfile.healthConditions.joinToString()}" else ""}
            ${if (petProfile.personality.isNotEmpty()) "- 성격: ${petProfile.personality.joinToString()}" else ""}
            ${if (petProfile.allergies.isNotEmpty()) "- 알레르기: ${petProfile.allergies.joinToString()}" else ""}
            ${if (petProfile.medications.isNotEmpty()) "- 복용 중인 약물: ${petProfile.medications.joinToString()}" else ""}
            
            위 정보를 바탕으로 이 반려동물에게 특화된 조언을 제공해주세요.
            """.trimIndent()
        } else {
            basePrompt
        }
    }
}