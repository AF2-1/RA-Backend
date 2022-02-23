package kr.co.tmax.rabackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import static com.google.common.collect.Lists.newArrayList;


@Configuration
public class SwaggerConfig {
    private static final String API_NAME = "Robo Advisor API";
    private static final String API_VERSION = "V1";
    private static final String API_DESCRIPTION = "Robo Advisor API 명세서";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .useDefaultResponseMessages(false)
                .globalResponses(HttpMethod.GET, newArrayList(
                        new ResponseBuilder().code("200")
                                .description("Ok").build(),
                        new ResponseBuilder().code("202")
                                .description("Accepted").build(),
                        new ResponseBuilder().code("403")
                                .description("Forbidden").build(),
                        new ResponseBuilder().code("404")
                                .description("Not Found").build(),
                        new ResponseBuilder().code("500")
                                .description("Internal Server Error").build()
                ))
                .globalResponses(HttpMethod.POST, newArrayList(
                        new ResponseBuilder().code("200")
                                .description("Ok").build(),
                        new ResponseBuilder().code("201")
                                .description("Created").build(),
                        new ResponseBuilder().code("202")
                                .description("Accepted").build(),
                        new ResponseBuilder().code("403")
                                .description("Forbidden").build(),
                        new ResponseBuilder().code("404")
                                .description("Not Found").build(),
                        new ResponseBuilder().code("500")
                                .description("Internal Server Error").build()
                ))
                .globalResponses(HttpMethod.DELETE, newArrayList(
                        new ResponseBuilder().code("200")
                                .description("Ok").build(),
                        new ResponseBuilder().code("403")
                                .description("Forbidden").build(),
                        new ResponseBuilder().code("404")
                                .description("Not Found").build(),
                        new ResponseBuilder().code("500")
                                .description("Internal Server Error").build()
                ))
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("kr.co.tmax.rabackend.interfaces"))
                .paths(PathSelectors.ant("/api/v1/**"))
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .build();
    }
}
