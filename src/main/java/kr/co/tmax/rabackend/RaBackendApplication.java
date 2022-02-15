package kr.co.tmax.rabackend;

import kr.co.tmax.rabackend.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
public class RaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(RaBackendApplication.class, args);
    }
}
