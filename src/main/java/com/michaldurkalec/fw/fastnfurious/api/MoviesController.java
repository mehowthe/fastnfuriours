package com.michaldurkalec.fw.fastnfurious.api;

import com.michaldurkalec.fw.fastnfurious.domain.Cinema;
import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.domain.dto.MovieDetails;
import com.michaldurkalec.fw.fastnfurious.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
