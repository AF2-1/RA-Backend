package kr.co.tmax.rabackend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class MongoConfig {
    @Bean
    public MongoClientSettingsBuilderCustomizer customizer() {
        return (builder) -> builder.applyToConnectionPoolSettings(
                (connectionPool) -> {
                    connectionPool.maxSize(20);
                    connectionPool.minSize(10);
                    connectionPool.maxConnectionIdleTime(10, TimeUnit.MINUTES);
                    connectionPool.maxWaitTime(10, TimeUnit.MINUTES);
                    connectionPool.maxConnectionLifeTime(30, TimeUnit.MINUTES);
                });
    }
}