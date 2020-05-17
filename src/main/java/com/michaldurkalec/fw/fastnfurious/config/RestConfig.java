package com.michaldurkalec.fw.fastnfurious.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.util.UriComponentsBuilder.fromUriString;

@Configuration
public class RestConfig {

    @Value("${rest.omdb.api.key}")
    private String omdbApiKey;

    @Value("${rest.omdb.api.url}")
    private String omdbApiUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        return objectMapper;
    }

    @Bean
    public WebClient omdbWebClient() {

        ExchangeStrategies strategies = ExchangeStrategies
                .builder()
                .codecs(configurer -> configurer.defaultCodecs()
                        .jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, APPLICATION_JSON)))
                .build();

        return WebClient.builder()
                .baseUrl(fromUriString(omdbApiUrl).queryParam("apikey", omdbApiKey).build().toUriString())
                .exchangeStrategies(strategies)
                .defaultHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                .build();
    }
}
