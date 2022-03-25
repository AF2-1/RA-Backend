package kr.co.tmax.rabackend;

import kr.co.tmax.rabackend.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
@EnableAsync
@EnableJpaRepositories(basePackages = {"kr.co.tmax.rabackend.infrastructure.user"})
@EnableMongoRepositories(basePackages = {"kr.co.tmax.rabackend.infrastructure.asset", "kr.co.tmax.rabackend.infrastructure.simulation",
        "kr.co.tmax.rabackend.infrastructure.strategy"})
public class RaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(RaBackendApplication.class, args);
    }
}
