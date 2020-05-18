package com.michaldurkalec.fw.fastnfurious.api;

import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.service.MovieRatingService;
import com.michaldurkalec.fw.fastnfurious.service.MovieService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Optional;

import static com.michaldurkalec.fw.fastnfurious.config.SpringFoxConfig.MOVIES_API_TAG;
import static com.michaldurkalec.fw.fastnfurious.config.SpringFoxConfig.MOVIES_RATING_API_TAG;
import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.status;

@Api(tags = {MOVIES_RATING_API_TAG})
@RestController
public class MoviesRatingController extends BaseMoviesRestController {

    @Autowired
    private MovieRatingService movieRatingService;
    @Autowired
    private MovieService movieService;

    @ApiOperation(value = "Get movie current avg rate", nickname = "Get movie current avg rate")
    @GetMapping("/{movieId}/rate")
    public ResponseEntity<Float> getMovieRate(@PathVariable(name = "movieId") String id) {
        Movie movie = movieService.findMovie(id);
        return ResponseEntity.of(ofNullable(movie).map(Movie::getAvgScore));
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 429, message = "Already rated")})
    @ApiOperation(value = "Add rate for a movie", nickname = "Add rate for a movie")
    @PostMapping("/{movieId}/rate")
    public ResponseEntity<Boolean> rateMovie(@PathVariable(name = "movieId") String id, @RequestBody RateRequest rate, HttpServletRequest request) {
        Optional<Boolean> rated = movieRatingService.rateMovie(id, rate.getScore(), request.getRemoteAddr());
        return rated.map(wasRated -> status(wasRated ? CREATED : TOO_MANY_REQUESTS))
                .map(x -> x.body(rated.get()))
                .orElseGet(() -> notFound().build());
    }

    @Data
    public static class RateRequest {
        @Min(value = 0, message = "Min score is 0")
        @Max(value = 5, message = "Max score is 5")
        private Integer score;
    }
}
