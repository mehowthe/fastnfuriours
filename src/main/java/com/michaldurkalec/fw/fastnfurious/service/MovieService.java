package com.michaldurkalec.fw.fastnfurious.service;

import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.domain.dto.MovieDetails;
import com.michaldurkalec.fw.fastnfurious.repository.MovieRepository;
import com.michaldurkalec.fw.fastnfurious.repository.MovieShowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieService {

    public static final int PAGE_LIMIT = 1000;

    @Autowired
    private OMDbClient omDbClient;
    @Autowired
    private MovieShowRepository movieShowRepository;
    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> listMovies() {
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);
        return movies;
    }

    public MovieDetails getMovieDetails(String id) {
        return MovieDetails.fromOMDbDetails(omDbClient.fetchMovieDetails(id));
    }

    public List<MovieShow> findMovieShows() {
        return movieShowRepository.findAll(PageRequest.of(0, PAGE_LIMIT)).getContent();
    }


}
