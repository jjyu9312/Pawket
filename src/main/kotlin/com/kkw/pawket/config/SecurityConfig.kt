package com.kkw.pawket.config

import com.kkw.pawket.common.filter.JwtAuthenticationFilter
import com.kkw.pawket.common.service.JwtTokenProvider
import com.kkw.pawket.common.service.OAuth2SuccessHandler
import com.kkw.pawket.user.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService
) {
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }  // REST API이므로 CSRF 보호 비활성화
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }  // JWT 사용을 위한 세션 비활성화
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(
                        "/user/login",
                        "/auth/**",
                        "/test/*",
                        "/common/*",
                    )
                    .permitAll()  // 로그인 및 OAuth2 관련 경로 허용
                    .anyRequest().authenticated()  // 나머지 경로는 인증 필요
            }
            .logout {
                it.logoutUrl("/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            }
            .oauth2Login { oauth ->
                oauth
                    .loginPage("/login")
                    .successHandler(OAuth2SuccessHandler(jwtTokenProvider, userService))
            }

        // JWT 필터 추가
        http.addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter::class.java,
        )

        return http.build()
    }
}