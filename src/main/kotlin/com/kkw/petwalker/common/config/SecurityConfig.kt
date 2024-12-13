package com.kkw.petwalker.common.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/", "/login", "/oauth2/**").permitAll()  // 로그인 및 OAuth2 관련 경로 허용
                    .anyRequest().authenticated()  // 나머지 경로는 인증 필요
            }
            .oauth2Login { oauth ->
                oauth
                    .loginPage("/login")  // 커스텀 로그인 페이지
                    .successHandler(OAuth2SuccessHandler(jwtTokenProvider)) // 로그인 성공 시 JWT 발급
            }

        // JWT 필터 추가
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}