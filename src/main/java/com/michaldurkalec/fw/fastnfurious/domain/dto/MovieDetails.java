package com.michaldurkalec.fw.fastnfurious.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDetails {

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
