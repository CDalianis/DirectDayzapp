package com.digitaldetox.core;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Digital Detox API")
                        .version("1.0.0")
                        .description("""
                                REST API for the Digital Detox coaching platform.
                                Authenticate via POST /api/v1/auth/authenticate to obtain a JWT bearer token.
                                """));
    }

    @Bean
    public OperationCustomizer globalSecurityResponses() {
        return (operation, handlerMethod) -> {
            boolean isSecured = handlerMethod.hasMethodAnnotation(SecurityRequirement.class)
                    || handlerMethod.getBeanType().isAnnotationPresent(SecurityRequirement.class);

            if (isSecured) {
                operation.getResponses()
                        .addApiResponse("401", new ApiResponse().description("Unauthorized"))
                        .addApiResponse("403", new ApiResponse().description("Forbidden"));
            }
            return operation;
        };
    }
}
