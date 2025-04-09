package com.kkw.pawket.config

import com.kkw.pawket.common.interceptor.AuthorizationInterceptor
import com.kkw.pawket.common.interceptor.LoggingInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfig(
    private val loggingInterceptor: LoggingInterceptor,
    private val authorizationInterceptor: AuthorizationInterceptor,
) : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loggingInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/test/**")
            .excludePathPatterns("/common/**")

        registry.addInterceptor(authorizationInterceptor)
            .addPathPatterns("/**")  // 모든 경로에 적용
            .excludePathPatterns("/test/**", "/common/**", "/auth/**")  // 인증이 필요 없는 경로 제외
    }
}