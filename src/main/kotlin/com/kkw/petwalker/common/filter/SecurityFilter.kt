package com.kkw.petwalker.common.filter

import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SecurityFilter : Filter {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun doFilter(
        request: ServletRequest,
        response: ServletResponse,
        chain: FilterChain
    ) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        logger.info("Security Filter - Request URI: ${httpRequest.requestURI}, Method: ${httpRequest.method}")
        logger.info("Security Filter - Response Status: ${httpResponse.status}")

        chain.doFilter(request, response)
    }
}
