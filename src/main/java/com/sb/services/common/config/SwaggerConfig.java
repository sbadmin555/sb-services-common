package com.sb.services.common.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Value("${swagger.title}")
    private String swaggerTitle;
    @Value("${swagger.description}")
    private String swaggerDescription;

    @Bean
    public Docket productApi() {

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Public Api")
                .select()
                .paths(PathSelectors.regex("/api/v1.*"))
                .build()
                .apiInfo(metaData());
    }

    private ApiInfo metaData() {
        ApiInfo apiInfo = new ApiInfo(
                swaggerTitle,
                swaggerDescription,
                "1.0",
                "Terms of service",
                new Contact("Supply Byte Inc.", null, null),
                "",
                "");
        return apiInfo;
    }

}
