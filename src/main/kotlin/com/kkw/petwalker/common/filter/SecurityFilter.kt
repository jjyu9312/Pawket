package com.kkw.petwalker.common.filter

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.Filter

@Component
class SecurityFilter : Filter {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpRequest = request as HttpServletRequest
        val httpResponse = response as HttpServletResponse

        logger.info("Security Filter - Request URI: ${httpRequest.requestURI}, Method: ${httpRequest.method}")
        logger.info("Security Filter - Response Status: ${httpResponse.status}")

        chain.doFilter(request, response)
    }
}
