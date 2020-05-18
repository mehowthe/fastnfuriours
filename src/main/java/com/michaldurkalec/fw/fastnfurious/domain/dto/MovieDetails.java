package com.michaldurkalec.fw.fastnfurious.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Transient;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(NON_NULL)
public class MovieDetails {

    @With
    @Transient
    private String movieId;
    @With
    @Transient
    private Float privateRating;

    private String name;
    private String description;
    private String releaseDate;
    private String rating;
    private String imdbRating;
    private String runtime;

    public static MovieDetails fromOMDbDetails(OMDbMovieDetails details) {
        return MovieDetails.builder()
                .name(details.getTitle())
                .description(details.getPlot())
                .releaseDate(details.getReleased())
                .rating(details.getMetascore())
                .imdbRating(details.getImdbRating())
                .runtime(details.getRuntime())
                .build();
    }
}
