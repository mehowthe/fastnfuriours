package com.michaldurkalec.fw.fastnfurious.service;

import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.domain.dto.MovieDetails;
import com.michaldurkalec.fw.fastnfurious.repository.CinemaRepository;
import com.michaldurkalec.fw.fastnfurious.repository.MovieRepository;
import com.michaldurkalec.fw.fastnfurious.repository.MovieShowRepository;
import lombok.experimental.Delegate;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Delegate
    @Autowired
    private CinemaRepository cinemaRepository;

    public MovieDetails getRemoteMovieDetails(String id) {
        return MovieDetails.fromOMDbDetails(omDbClient.fetchMovieDetails(id));
    }

    public List<Movie> listMovies() {
        List<Movie> movies = new ArrayList<>();
        movieRepository.findAll().forEach(movies::add);
        return movies;
    }

    public Movie findMovie(String id) {
        return movieRepository.findById(id).orElse(null);
    }

    public List<MovieShow> findMovieShowsByMovie(String movieId) {
        return movieShowRepository.findByMovie(movieId);
    }

    public List<MovieShow> findMovieShowsByCinema(Long cinemaId) {
        return movieShowRepository.findByCinema(cinemaId);
    }

}
