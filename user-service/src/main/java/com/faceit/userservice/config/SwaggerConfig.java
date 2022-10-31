package com.faceit.userservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "FACEIT User Microservice", description = "A user management microservice for FACEIT"))
public class SwaggerConfig {
}
