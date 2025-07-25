package com.kkw.pawket.config

import com.kkw.pawket.common.filter.JwtAuthenticationFilter
import com.kkw.pawket.common.service.JwtTokenProvider
import com.kkw.pawket.common.service.OAuth2SuccessHandler
import com.kkw.pawket.user.domain.UserRole
import com.kkw.pawket.user.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService
) {

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web ->
            web.ignoring()
                .requestMatchers(
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/favicon.ico",
                    "/error"
                )
        }
    }

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }  // REST API이므로 CSRF 보호 비활성화
            .cors { cors ->  // CORS 설정 수정
                cors.configurationSource {
                    val configuration = CorsConfiguration()
                    configuration.allowedOrigins = listOf(
                        "http://localhost:8080",
                        "http://127.0.0.1:8080",
                        "https://frontend-domain.com"
                    )
                    configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    configuration.allowedHeaders = listOf("*")
                    configuration.allowCredentials = true
                    configuration
                }
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }  // JWT 사용을 위한 세션 비활성화
            .authorizeHttpRequests { auth ->
                auth
                    // 관리자 권한 필요
                    .requestMatchers("/private/v1/**").hasRole(UserRole.ADMIN.name)

                    // 일반 사용자 권한 필요
                    .requestMatchers("/api/v1/**").hasRole(UserRole.USER.name)

                    // 공개 접근 허용 - Swagger 관련
                    .requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                    ).permitAll()

                    // 공개 접근 허용 - 인증 관련
                    .requestMatchers(
                        "/api/v1/user/login",
                        "/api/v1/login/*",
                        "/api/v1/oauth2/authorization/*",  // OAuth 인증 시작 경로
                        "/api/v1/login/oauth2/code/*",  // OAuth 인증 완료 경로
                        "/api/v1/user/logout", // 로그아웃 경로
                    ).permitAll()

                    // 공개 접근 허용 - 기타
                    .requestMatchers(
                        "/test/**",
                        "/common/**",
                        "/health",
                        "/actuator/**",
                    ).permitAll()

                    // 나머지 경로는 인증 필요
                    .anyRequest().authenticated()
            }
            .logout {
                it.logoutUrl("/api/v1/user/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            }
//            .oauth2Login { oauth ->
//                oauth
//                    .loginPage("/api/v1/login")
//                    .authorizationEndpoint { endpoint ->
//                        endpoint.baseUri("/api/v1/oauth2/authorization")
//                    }
//                    .redirectionEndpoint { endpoint ->
//                        endpoint.baseUri("/api/v1/login/oauth2/code/*")
//                    }
//                    .successHandler(OAuth2SuccessHandler(jwtTokenProvider, userService))
//            }

        // JWT 필터 추가
        http.addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter::class.java,
        )

        return http.build()
    }
}