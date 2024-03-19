package com.java.nuven.application.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
            .addSecurityItem(
                    new SecurityRequirement().addList("bearerAuth"))
            .info(new Info().title("Todo API")
                .description("API for managing todos")
                .version("v0.0.1")
                .contact(new Contact()
                    .email("arthursqueiroz713@gmail.com")
                    .name("Arthur Silva Queiroz"))
            );
    }
}
