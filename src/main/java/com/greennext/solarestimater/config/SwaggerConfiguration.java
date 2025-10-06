package com.greennext.solarestimater.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class SwaggerConfiguration {
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                        .title("GreenNxt")
                        .description("GreenNxt")
                        .version("1.0")
                        .license(new io.swagger.v3.oas.models.info.License()
                                .name("Apache 2.0")
                                .url("https://springdoc.org"))
                        .contact(new Contact().name("GreenNxt Support").email("support@green-nxt.com"))
                )
                .addServersItem(new Server().url("/").description("Default Server URL"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme().name("bearerAuth")
                        .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }

}
