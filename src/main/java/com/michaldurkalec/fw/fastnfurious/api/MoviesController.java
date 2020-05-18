package com.michaldurkalec.fw.fastnfurious.api;

import com.michaldurkalec.fw.fastnfurious.domain.Cinema;
import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.domain.dto.MovieDetails;
import com.michaldurkalec.fw.fastnfurious.service.MovieRatingService;
import com.michaldurkalec.fw.fastnfurious.service.MovieService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

@RestController
public class MoviesController extends BaseMoviesRestController {

    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieRatingService movieRatingService;

    @GetMapping("/")
    public List<Movie> listMovies() {
        return movieService.listMovies();
    }

    @GetMapping("/{movieId}")
    public MovieDetails getMovieDetails(@PathVariable(name = "movieId") String id, @RequestParam(name = "extended", defaultValue = "false") boolean extended) {
        MovieDetails movieDetails = movieService.getRemoteMovieDetails(id);
        if (!extended) {
            return movieDetails;
        }
        Movie movie = movieService.findMovie(id);
        return movieDetails
                .withMovieId(movie.getMovieId())
                .withPrivateRating(movie.getAvgScore());
    }

    @PostMapping("{movieId}/rate")
    public ResponseEntity<?> rateMovie(@PathVariable(name = "movieId") String id, @RequestBody RateRequest rate, HttpServletRequest request) {
        if (movieRatingService.rateMovie(id, rate.getScore(), request.getRemoteAddr())) {
            return ResponseEntity.status(CREATED).build();
        } else {
            return ResponseEntity.status(TOO_MANY_REQUESTS).build();
        }
    }

    @GetMapping("/shows")
    public List<MovieShow> getMovieShowsByMovieId(@RequestParam(name = "id") String id) {
        return movieService.findMovieShowsByMovie(id);
    }

    @GetMapping("/cinemas")
    public Set<Cinema> getCinemas() {
        Set<Cinema> cinemas = new HashSet<>();
        movieService.findAll().forEach(cinemas::add);
        return cinemas;
    }

    @GetMapping("cinemas/program")
    public List<MovieShow> getShowsForCinema(@RequestParam(name = "id") Long id) {
        return movieService.findMovieShowsByCinema(id);
    }

    @Data
    public static class RateRequest {
        private Float score;
    }

}
