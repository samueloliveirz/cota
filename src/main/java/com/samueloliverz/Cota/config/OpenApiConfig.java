package com.samueloliverz.Cota.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI cotaOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Cota API")
                        .description("API de criação e acompanhamento de orçamentos comerciais")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Samuel Oliveira")
                                .url("https://github.com/samueloliveirz")));
    }
}