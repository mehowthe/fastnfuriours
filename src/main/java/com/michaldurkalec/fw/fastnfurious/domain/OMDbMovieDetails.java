package com.michaldurkalec.fw.fastnfurious.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;
import static lombok.AccessLevel.PRIVATE;

@Data
@FieldDefaults(level = PRIVATE)
@JsonFormat(with = ACCEPT_CASE_INSENSITIVE_PROPERTIES)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OMDbMovieDetails {

    String Title;
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
    @JsonFormat(with = ACCEPT_CASE_INSENSITIVE_PROPERTIES)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rating {
        String source;
        String value;
    }
}
