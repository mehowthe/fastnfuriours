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

    /**
     * @return  Optional.empty() - if movie not found
     *          Optional.of(true) - if movie was rated
     *          Optional.of(false) - if movie was already rated
     */
    public Optional<Boolean> rateMovie(String movieId, Integer score, String userIp) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (!movie.isPresent()) {
            return Optional.empty();
        }
        Optional<Rating> rating = ratingRepository.findOneByMovieIdAndUserIp(movieId, userIp);
        return rating.map(ignore -> Optional.of(false))
                .orElseGet(() -> Optional.of(newRating(score, userIp, movie.get())));
    }

    private Boolean newRating(Integer score, String userIp, Movie movie) {
        ratingRepository.save(Rating.builder()
                .movie(movie)
                .score(score)
                .userIp(userIp)
                .build());
        return true;
    }
}
