package com.michaldurkalec.fw.fastnfurious.api;

import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.domain.dto.MovieDetails;
import com.michaldurkalec.fw.fastnfurious.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MoviesController extends BaseMoviesRestController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/")
    public List<Movie> listMovies() {
        return movieService.listMovies();
    }

    @GetMapping("/details")
    public MovieDetails getMovieDetails(@RequestParam(name = "id") String id) {
        return movieService.getMovieDetails(id);
    }

    @GetMapping("/shows")
    public List<MovieShow> getMovieShowsByMovieId(@RequestParam(name = "id") String id) {
        return movieService.findMovieShows();
    }
}
