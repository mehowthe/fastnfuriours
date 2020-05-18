package com.michaldurkalec.fw.fastnfurious.api.priv;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.service.MovieCatalogService;
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

import static org.springframework.http.ResponseEntity.accepted;

@RestController
public class MovieCatalogController extends BaseProtectedMovieController {

    @Autowired
    private MovieCatalogService movieCatalogService;

    // TODO: add user management - only cinema owners/managers assigned to cinema should be able to update their program
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
