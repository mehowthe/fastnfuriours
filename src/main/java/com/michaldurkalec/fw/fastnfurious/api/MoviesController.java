package com.michaldurkalec.fw.fastnfurious.api;

import com.michaldurkalec.fw.fastnfurious.domain.Cinema;
import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.domain.dto.MovieDetails;
import com.michaldurkalec.fw.fastnfurious.service.MovieRatingService;
import com.michaldurkalec.fw.fastnfurious.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("{id}/rate")
    public ResponseEntity rateMovie(@PathVariable(name = "id") String id, HttpServletRequest request) {
        if (movieRatingService.rateMovie(id, request.getRemoteAddr())) {
            return ResponseEntity.status(CREATED).build();
        } else {
            return ResponseEntity.status(TOO_MANY_REQUESTS).build();
        }
    }

    @GetMapping("/details")
    public MovieDetails getMovieDetails(@RequestParam(name = "id") String id) {
        return movieService.getMovieDetails(id);
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


}
