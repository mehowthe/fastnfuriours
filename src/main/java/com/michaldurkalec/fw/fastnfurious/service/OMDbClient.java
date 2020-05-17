package com.michaldurkalec.fw.fastnfurious.service;

import com.michaldurkalec.fw.fastnfurious.domain.OMDbMovieDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class OMDbClient {

    @Autowired
    private WebClient webClient;

    public OMDbMovieDetails fetchMovieDetails(String id) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.queryParam("i", id).build())
                .retrieve()
                .bodyToMono(OMDbMovieDetails.class)
                .block();

    }
}
