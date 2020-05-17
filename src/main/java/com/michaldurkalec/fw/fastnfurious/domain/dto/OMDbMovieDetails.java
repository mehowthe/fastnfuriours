package com.michaldurkalec.fw.fastnfurious.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OMDbMovieDetails {

    String title;
    String year;
    String rated;
    String released;
    String runtime;
    String genre;
    String director;
    String writer;
    String actors;
    String plot;
    String language;
    String country;
    String awards;
    String poster;
    List<Rating> ratings = null;
    String metascore;
    String imdbRating;
    String imdbVotes;
    String imdbID;
    String type;
    String dVD;
    String boxOffice;
    String production;
    String website;
    String response;

    @Data
    @FieldDefaults(level = PRIVATE)
    public static class Rating {
        String source;
        String value;
    }
}
