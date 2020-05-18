package com.michaldurkalec.fw.fastnfurious.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.DETACH;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private Float score;

    @ManyToOne(cascade = DETACH)
    @JoinColumn(name = "MOVIE_ID", referencedColumnName = "MOVIE_ID")
    private Movie movie;

    private String userIp;
}
