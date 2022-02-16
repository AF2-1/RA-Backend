package kr.co.tmax.rabackend.config;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Configuration
public class AppConfig {

    private final AppProperties appProperties;

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(appProperties.getAi().getUrl())
                .defaultHeader("Host", appProperties.getAi().getHost())
                .build();
    }
}
