package com.michaldurkalec.fw.fastnfurious.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Movie {

    @Id
    @Column(name = "MOVIE_ID")
    private String movieId;
    private String title;
}
