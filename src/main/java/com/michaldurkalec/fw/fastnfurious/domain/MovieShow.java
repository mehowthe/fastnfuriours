package com.michaldurkalec.fw.fastnfurious.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.CascadeType.DETACH;

@Data
@Entity
@Table(name = "MOVIE_SHOW")
public class MovieShow {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = DETACH)
    @JoinColumn(name = "MOVIE_ID", referencedColumnName = "MOVIE_ID")
    private Movie movie;
    @ManyToOne
    @JoinColumn(name="CINEMA_ID", nullable = false)
    private Cinema cinema;

    private Double price;
    @Temporal(TemporalType.TIMESTAMP)
    private Date time;


}
