package com.kkw.pawket.config

import com.kkw.pawket.common.filter.JwtAuthenticationFilter
import com.kkw.pawket.common.service.JwtTokenProvider
import com.kkw.pawket.common.service.OAuth2SuccessHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtTokenProvider: JwtTokenProvider,

    ) { // 역할: Spring Security 설정

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/user/login",
                        "/auth/**",
                        "/test/*",
                        "/common/*",
                    ) // 허용할 경로
                    .permitAll()  // 로그인 및 OAuth2 관련 경로 허용
                    .anyRequest().authenticated()  // 나머지 경로는 인증 필요
            }
            .logout {
                it.logoutUrl("/logout") // 로그아웃 URL 설정
                    .logoutSuccessUrl("/") // 로그아웃 성공 후 이동할 URL
                    .invalidateHttpSession(true) // 세션 무효화
                    .deleteCookies("JSESSIONID") // 쿠키 삭제
            }
            .oauth2Login { oauth ->
                oauth
                    .loginPage("/login")  // 커스텀 로그인 페이지
                    .successHandler(OAuth2SuccessHandler(jwtTokenProvider)) // 로그인 성공 시 JWT 발급
            }

        // JWT 필터 추가
        http.addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter::class.java,
        )

        return http.build()
    }
}