package com.raizesdonordeste.api.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI personalizacaoOpenAPI(){

        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
        .info(new Info()
            .title("Raízes do Nodeste - API de Cadastro de Usuários e Pedidos")
            .version("1.0.0-MVP")
            .description("API desenvolvida para o Projeto Multidisciplinar da Graduação em Análise e Desenvolvimento de Sistemas "
                        +"do Centro Universitário Internacional - Uninter. "
                        +"Gerencia o cadastramento de usuários e o fluxo crítico de vendas, validando a disponibilidade dos produtos "
                        +"no cardápio da unidade e controlando automaticamente os estoques de produtos e ingredientes."))
        .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
        .components(new Components()
            .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                .name(securitySchemeName)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")));
    }
}
