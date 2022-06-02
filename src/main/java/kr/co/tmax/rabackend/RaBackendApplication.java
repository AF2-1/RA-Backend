package kr.co.tmax.rabackend;

import kr.co.tmax.rabackend.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableConfigurationProperties(AppProperties.class)
@SpringBootApplication
@EnableAsync
//@EnableJpaRepositories(basePackages = "kr.co.tmax.rabackend.infrastructure.user")
//@EnableMongoRepositories(basePackages = {"kr.co.tmax.rabackend.infrastructure.simulation",
//        "kr.co.tmax.rabackend.infrastructure.strategy", "kr.co.tmax.rabackend.infrastructure.asset"})
@EnableCaching
public class RaBackendApplication {
    @PostConstruct
    public void started() {
        // timezone UTC 셋팅
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        SpringApplication.run(RaBackendApplication.class, args);
    }
}
