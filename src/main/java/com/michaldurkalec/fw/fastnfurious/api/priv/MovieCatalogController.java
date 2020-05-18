package com.michaldurkalec.fw.fastnfurious.api.priv;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.service.MovieCatalogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.michaldurkalec.fw.fastnfurious.config.SpringFoxConfig.MOVIES_CINEMA_CATALOG_API_TAG;
import static org.springframework.http.ResponseEntity.accepted;

@Api(tags = {MOVIES_CINEMA_CATALOG_API_TAG})
@RestController
public class MovieCatalogController extends BaseProtectedMovieController {

    @Autowired
    private MovieCatalogService movieCatalogService;

    // TODO: add user management - only cinema owners/managers assigned to cinema should be able to update their program
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Success"),
            @ApiResponse(code = 404, message = "Not found")})
    @ApiOperation(value = "Update cinema program", nickname = "Update cinema program",
            notes = "If showId is null, then new movie show entry will be created. Otherwise - it will update existing")
    @PutMapping("/shows/{cinemaId}")
    public ResponseEntity<Set<MovieShow>> updateShows(@PathVariable(name = "cinemaId") Long cinemaId, @RequestBody List<ShowUpdate> updates) {
        Optional<Set<MovieShow>> result = movieCatalogService.updateMovieShow(cinemaId, updates);
        return result.map(movieShows -> accepted().body(result.get()))
                .orElse(ResponseEntity.notFound().build());
    }

    @Data
    public static class ShowUpdate {

        private Long showId;
        private String movieId;
        private Double price;
        private boolean remove;

        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "dd-MM-yyyy hh:mm:ss")
        @Temporal(TemporalType.TIMESTAMP)
        private Date time;
    }
}
