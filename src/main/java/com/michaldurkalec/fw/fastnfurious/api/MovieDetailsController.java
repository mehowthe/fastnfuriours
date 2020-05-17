package com.michaldurkalec.fw.fastnfurious.api;

import com.michaldurkalec.fw.fastnfurious.domain.MovieDetails;
import com.michaldurkalec.fw.fastnfurious.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieDetailsController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/details")
    public MovieDetails getMovieDetails(@RequestParam(name = "id") String id) {
        return movieService.getMovieDetails(id);
    }
}
