package concept.com.example.club.common.security;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("ROM.Concept API")
                        .version("1.0")
                        .description("Documentação oficial da API do clube de luxo ROM.Concept. Inclui integrações de IoT (Totem NFC) e WebSockets.")
                        .contact(new Contact().name("Seu Nome").email("seuemail@romconcept.com")))
                // Adiciona o botão de "Authorize" global no topo da página
                .servers(List.of(
                        //new Server().url("https://romsociety-830621320948.southamerica-east1.run.app") //prod
                        new Server().url("https://testeromsociety-830621320948.southamerica-east1.run.app") // dev
                                .description("Servidor de Produção (HTTPS)")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

}