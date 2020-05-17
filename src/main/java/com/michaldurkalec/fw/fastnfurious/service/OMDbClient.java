package com.michaldurkalec.fw.fastnfurious.service;

import com.michaldurkalec.fw.fastnfurious.domain.OMDbMovieDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.springframework.web.util.UriComponentsBuilder.fromUri;

@Component
public class OMDbClient {

    private static final String APIKEY_PARAM = "apikey";
    private static final String ID_PARAM = "i";

    @Value("${rest.omdb.api.key}")
    private String omdbApiKey;

    @Value("${rest.omdb.api.url}")
    private URI omdbApiUrl;

    @Autowired
    private RestTemplate restTemplate;

    public OMDbMovieDetails fetchMovieDetails(String id) {
        return restTemplate.getForObject(fromUri(omdbApiUrl)
                .queryParam(APIKEY_PARAM, omdbApiKey)
                .queryParam(ID_PARAM, id).toUriString(), OMDbMovieDetails.class);

    }
}
