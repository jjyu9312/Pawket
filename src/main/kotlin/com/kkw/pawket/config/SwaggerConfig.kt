package com.kkw.pawket.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("pawket API")
                    .version("1.0")
                    .description("pawket 애플리케이션 API 문서"))
            .components(
                Components()
                    .addSecuritySchemes("bearer-jwt",
                        SecurityScheme()
                            .type(SecurityScheme.Type.HTTP)
                            .scheme("bearer")
                            .bearerFormat("JWT")
                            .`in`(SecurityScheme.In.HEADER)
                            .name("Authorization")))
            .addSecurityItem(SecurityRequirement().addList("bearer-jwt"))
    }

    @Bean
    fun adminOpenApiCustomizer(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("admin")
            .pathsToMatch("/private/v1/**")
            .build()
    }

    @Bean
    fun userOpenApiCustomizer(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("user")
            .pathsToMatch("/api/v1/**")
            .build()
    }
}