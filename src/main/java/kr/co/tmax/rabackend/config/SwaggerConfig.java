package kr.co.tmax.rabackend.config;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;


@Configuration
public class SwaggerConfig {
    private static final String API_NAME = "Robo Advisor API";
    private static final String API_VERSION = "V1";
    private static final String API_DESCRIPTION = "Robo Advisor API 명세서";

    @Bean
    public Docket api() {
        TypeResolver typeResolver = new TypeResolver();

        return new Docket(DocumentationType.SWAGGER_2)
                .alternateTypeRules(
                        AlternateTypeRules.newRule(typeResolver.resolve(Pageable.class), typeResolver.resolve(Page.class))
                )
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
                .build()
                .securityContexts(Arrays.asList(securityContext()))
                .securitySchemes(Arrays.asList(apiKey()));
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(API_NAME)
                .version(API_VERSION)
                .description(API_DESCRIPTION)
                .build();
    }

    @Getter
    @ApiModel
    static class Page {
        @ApiModelProperty(value = "페이지 번호(1부터 시작)", example = "1")
        private Integer page;

        @ApiModelProperty(value = "페이지 당 요소 개수(최대 100, 기본 20)", allowableValues = "range[0, 100]", example = "20")
        private Integer size;
    }
}
