package com.michaldurkalec.fw.fastnfurious.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Objects;

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
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "movie")
    private List<Rating> ratings;

    @JsonIgnore
    public Float getAvgScore() {
        Double avgScore = ratings.stream()
                .filter(Objects::nonNull)
                .mapToDouble(Rating::getScore)
                .average()
                .orElse(Double.NaN);
        return avgScore.isNaN()
                ? null
                : avgScore.floatValue();
    }
}
