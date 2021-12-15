package kr.co.tmax.rabackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();

    @Setter
    @Getter
    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
    }
}
