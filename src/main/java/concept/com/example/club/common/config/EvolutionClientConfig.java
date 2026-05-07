package concept.com.example.club.common.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EvolutionClientConfig {

    @Value("${evolution.api.key}")
    private String apiKey;

    @Bean
    public RequestInterceptor requestInterceptor(){
        return requestTemplate -> requestTemplate
                .header("apiKey", apiKey)
                .header("Content-Type", "application/json");
    }
}
