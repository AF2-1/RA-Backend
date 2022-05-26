package kr.co.tmax.rabackend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@Getter
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private final Auth auth = new Auth();
    private final Simulation simulation = new Simulation();
    private final Ai ai = new Ai();
    private final Trading trading = new Trading();
    private final Info info = new Info();

    @Setter
    @Getter
    public static class Auth {
        private String tokenSecret;
        private long tokenExpirationMsec;
    }

    @Setter
    @Getter
    public static class Simulation {
        private Set<String> strategies;
    }

    @Setter
    @Getter
    public static class Ai {
        private String url;
        private String path;
        private String host;
        private String callBackUrl;
    }

    @Getter
    @Setter
    public static class Trading {
        private String engineAddress;
        private String path;
        private String callBackUrl;
    }

    @Getter
    @Setter
    public static class Info {
        private String url;
    }
}
