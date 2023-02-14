package com.quick.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * @author CShisan
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi platformApi() {
        return GroupedOpenApi.builder().group("platform-group").pathsToMatch("/platform/**").build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder().group("auth-group").pathsToMatch("/auth/**").build();
    }

    @Bean
    public OpenAPI openApi() {
        SecurityScheme securityScheme = new SecurityScheme()
                .name(HttpHeaders.AUTHORIZATION)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
        Components components = new Components().addSecuritySchemes(HttpHeaders.AUTHORIZATION, securityScheme);
        return new OpenAPI().info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                .components(components);
    }

    private Info apiInfo() {
        Contact contact = new Contact().name("CShisan").email("xxxxx@xx.com").url("http://localhost:8013/swagger-ui/");
        License license = new License().name("Apache 2.0").url("https://springdoc.org");
        return new Info().contact(contact).title("Quick-Launch接口文档").description("墙头草,两边倒").version("v0.0.1").license(license);
    }
}
