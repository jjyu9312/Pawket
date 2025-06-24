package com.kkw.pawket.ai.service

import com.kkw.pawket.ai.model.common.ChatMessage
import org.apache.coyote.BadRequestException
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class ChatSessionService {

    // 실제로는 Redis나 DB 사용 권장
    private val sessions = ConcurrentHashMap<String, MutableList<ChatMessage>>()
    private val sessionUsers = ConcurrentHashMap<String, String>() // sessionId -> userId
    private val userSessions = ConcurrentHashMap<String, MutableList<String>>() // userId -> sessionIds
    private val maxContextLength = 10 // 최근 10개 메시지만 유지

    fun createNewSession(userId: String): String {
        val sessionId = UUID.randomUUID().toString()

        sessions[sessionId] = mutableListOf()
        sessionUsers[sessionId] = userId
        userSessions.getOrPut(userId) { mutableListOf() }.add(sessionId)

        return sessionId
    }

    fun getContext(sessionId: String): List<ChatMessage> {
        return sessions[sessionId]?.takeLast(maxContextLength) ?: emptyList()
    }

    fun getContextWithValidation(userId: String, sessionId: String): List<ChatMessage> {
        validateSessionOwnership(userId, sessionId)
        return getContext(sessionId)
    }

    fun updateSession(sessionId: String, userMessage: String, assistantReply: String) {
        val context = sessions[sessionId] ?: throw BadRequestException("존재하지 않는 세션입니다.")

        context.add(ChatMessage("user", userMessage))
        context.add(ChatMessage("assistant", assistantReply))

        // 컨텍스트 길이 제한
        while (context.size > maxContextLength * 2) {
            context.removeFirst()
        }
    }

    fun clearSession(sessionId: String) {
        sessions.remove(sessionId)
        val userId = sessionUsers.remove(sessionId)
        userId?.let { userSessions[it]?.remove(sessionId) }
    }

    fun clearSessionWithValidation(userId: String, sessionId: String) {
        validateSessionOwnership(userId, sessionId)
        clearSession(sessionId)
    }

    fun getActiveSessionsByUserId(userId: String): List<String> {
        return userSessions[userId]?.toList() ?: emptyList()
    }

    fun getAllSessions(): Set<String> {
        return sessions.keys
    }

    private fun validateSessionOwnership(userId: String, sessionId: String) {
        val sessionOwner = sessionUsers[sessionId]
            ?: throw BadRequestException("존재하지 않는 세션입니다.")

        if (sessionOwner != userId) {
            throw BadRequestException("해당 세션에 접근할 권한이 없습니다.")
        }
    }
}