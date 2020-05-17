package com.michaldurkalec.fw.fastnfurious.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Movie {

    @Id
    @Column(name = "MOVIE_ID")
    private String movieId;
    private String title;

    @JsonIgnore
    @OneToMany(mappedBy = "movie")
    private List<Rating> ratings;

}
