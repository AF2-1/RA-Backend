package kr.co.tmax.rabackend;

import kr.co.tmax.rabackend.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@EnableAsync
//@EnableJpaRepositories(basePackages = "kr.co.tmax.rabackend.infrastructure.user")
//@EnableMongoRepositories(basePackages = {"kr.co.tmax.rabackend.infrastructure.simulation",
//        "kr.co.tmax.rabackend.infrastructure.strategy", "kr.co.tmax.rabackend.infrastructure.asset"})
@EnableCaching
public class RaBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(RaBackendApplication.class, args);
    }
}
