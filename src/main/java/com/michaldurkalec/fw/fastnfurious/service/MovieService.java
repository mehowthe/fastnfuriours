package com.michaldurkalec.fw.fastnfurious.service;

import com.michaldurkalec.fw.fastnfurious.domain.MovieDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MovieService {

    @Autowired
    private OMDbClient omDbClient;

    public MovieDetails getMovieDetails(String id) {
        return MovieDetails.fromOMDbDetails(omDbClient.fetchMovieDetails(id));
    }


}
