package com.michaldurkalec.fw.fastnfurious.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static java.util.Collections.emptyList;
import static springfox.documentation.builders.PathSelectors.any;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;

@Configuration
@Import(BeanValidatorPluginsConfiguration.class)
@EnableSwagger2
public class SpringFoxConfig {

    public static final String MOVIES_API_TAG = "Movies API";
    public static final String MOVIES_RATING_API_TAG = "Movies rating API";
    public static final String MOVIES_CINEMA_CATALOG_API_TAG = "Movie Cinema Catalog API";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(basePackage("com.michaldurkalec.fw.fastnfurious.api"))
                .paths(any())
                .build()
                .apiInfo(apiInfo())
                .tags(new Tag(MOVIES_API_TAG, "List movies, get movie description, get movie times in cinemas"))
                .tags(new Tag(MOVIES_RATING_API_TAG, "Rate a movie"))
                .tags(new Tag(MOVIES_CINEMA_CATALOG_API_TAG, "Update show times and prices for movies in cinames"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Fast & Furious movies API",
                "Backend Coding Challenge",
                "v1",
                null,
                new Contact("Michal Durkalec", "www.michaldurkalec.com", "madurkalec@gmail.com"),
                null, null, emptyList());
    }
}
