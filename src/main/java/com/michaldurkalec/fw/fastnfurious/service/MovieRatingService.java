package com.michaldurkalec.fw.fastnfurious.service;

import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.Rating;
import com.michaldurkalec.fw.fastnfurious.repository.MovieRepository;
import com.michaldurkalec.fw.fastnfurious.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MovieRatingService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private RatingRepository ratingRepository;

    public boolean rateMovie(String movieId, Float score, String userIp) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (!movie.isPresent()) {
            throw new IllegalArgumentException("Movie doesn't exists");
        }
        Optional<Rating> rating = ratingRepository.findOneByMovieIdAndUserIp(movieId, userIp);
        return rating.map(ignore -> false)
                .orElseGet(() -> newRating(score, userIp, movie.get()));
    }

    private Boolean newRating(Float score, String userIp, Movie movie) {
        ratingRepository.save(Rating.builder()
                .movie(movie)
                .score(score)
                .userIp(userIp)
                .build());
        return true;
    }
}
