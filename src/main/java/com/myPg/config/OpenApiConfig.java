package com.myPg.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "bearerAuth";

    @Bean
    public OpenAPI customOpenAPI() {
        // Create OpenAPI instance
        OpenAPI openAPI = new OpenAPI()
                .info(new Info()
                        .title("My PG API")
                        .description("PG Management Application APIs")
                        .version("1.0"))
                // Add ngrok server
                .addServersItem(new Server()
                        .url("https://acetylic-kristel-pretentiously.ngrok-free.dev")
                        .description("Ngrok HTTPS server"))
                // Add security requirement globally
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        ));

        return openAPI;
    }
}
