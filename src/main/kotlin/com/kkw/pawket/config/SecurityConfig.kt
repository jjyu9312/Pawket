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
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }  // REST API이므로 CSRF 보호 비활성화
            .cors { cors ->  // CORS 설정 추가
                cors.configurationSource {
                    val configuration = CorsConfiguration()
                    configuration.allowedOrigins = listOf("https://frontend-domain.com")
                    configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    configuration.allowedHeaders = listOf("*")
                    configuration.allowCredentials = true
                    configuration
                }
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }  // JWT 사용을 위한 세션 비활성화
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/private/v1/**").hasRole(UserRole.ADMIN.name)
                    .requestMatchers("/api/v1/**").hasRole(UserRole.USER.name)
                    .requestMatchers(
                        "/api/v1/user/login",
                        "/api/v1/login/*",
                        "/api/v1/oauth2/authorization/*",  // OAuth 인증 시작 경로
                        "/api/v1/login/oauth2/code/*",  // OAuth 인증 완료 경로
                        "/api/v1/user/logout",  // 로그아웃 경로
                        "/api/v1/swagger-ui/",         // Swagger UI (있는 경우)
                        "/api/v1/v3/api-docs/",
                        "/test/*",
                        "/common/*",
                    ).permitAll()  // 로그인 및 OAuth2 관련 경로 허용
                    .anyRequest().authenticated()  // 나머지 경로는 인증 필요
            }
            .logout {
                it.logoutUrl("/api/v1/user/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            }
            .oauth2Login { oauth ->
                oauth
                    .loginPage("/api/v1/login")
                    .authorizationEndpoint { endpoint ->
                        endpoint.baseUri("/api/v1/oauth2/authorization")
                    }
                    .redirectionEndpoint { endpoint ->
                        endpoint.baseUri("/api/v1/login/oauth2/code/*")
                    }
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