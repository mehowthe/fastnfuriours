package com.michaldurkalec.fw.fastnfurious.service;

import com.michaldurkalec.fw.fastnfurious.domain.Cinema;
import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.repository.CinemaRepository;
import com.michaldurkalec.fw.fastnfurious.repository.MovieRepository;
import com.michaldurkalec.fw.fastnfurious.repository.MovieShowRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;

import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

@SpringBootTest
public class MovieServiceTest {

    private static final String MOVIE_ID = "test_id";
    private static final String MOVIE_ID2 = "test_id2";
    private static final String MOVIE_NAME = "F & F";
    private static final String CINEMA_NAME = "AMC";

    private Movie testMovie1 = new Movie(MOVIE_ID, MOVIE_NAME, emptyList());
    private Movie testMovie2 = new Movie(MOVIE_ID2, MOVIE_NAME, emptyList());
    private Cinema testCinema1 = Cinema.builder()
            .name(CINEMA_NAME)
            .address("Address")
            .build();
    private Cinema testCinema2 = testCinema1.toBuilder()
            .name(CINEMA_NAME + "2")
            .build();

    private MovieShow testMovieShow1 = MovieShow.builder()
            .movie(testMovie1)
            .cinema(testCinema1)
            .price(30.0)
            .time(Date.from(now()))
            .build();

    private MovieShow testMovieShow2 = testMovieShow1.toBuilder()
            .movie(testMovie2)
            .cinema(testCinema2)
            .build();

    @Autowired
    private MovieShowRepository movieShowRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private MovieService sut;

    @BeforeEach
    public void setUp() {
        testCinema1 = cinemaRepository.save(testCinema1);
        testCinema2 = cinemaRepository.save(testCinema2);
        testMovie1 = movieRepository.save(testMovie1);
        testMovie2 = movieRepository.save(testMovie2);
        testMovieShow1 = movieShowRepository.save(testMovieShow1);
        testMovieShow2 = movieShowRepository.save(testMovieShow2);
    }

    @AfterEach
    public void tearDown() {
        movieShowRepository.deleteAll(asList(testMovieShow1, testMovieShow2));
        movieRepository.deleteAll(asList(testMovie1, testMovie2));
        cinemaRepository.deleteAll(asList(testCinema1, testCinema2));
    }

    @Test
    public void shouldListMovies() {
        List<Movie> movies = sut.listMovies();
        assertThat(movies, hasItems(testMovie1, testMovie2));
    }

    @Test
    public void shouldFindMovieShowsByMovie() {
        List<MovieShow> movieShows = sut.findMovieShowsByMovie(MOVIE_ID);
        assertThat(movieShows, hasSize(1));
        MovieShow movieShow = movieShows.get(0);
        assertThat(movieShow.getMovie().getMovieId(), is(MOVIE_ID));
        assertThat(movieShow.getCinema().getName(), is(CINEMA_NAME));
    }

    @Test
    public void shouldFindMovieShowsByCinema() {
        List<MovieShow> movieShows = sut.findMovieShowsByCinema(testCinema1.getId());
        assertThat(movieShows, hasSize(1));
        assertThat(movieShows.get(0).getCinema().getName(), is(CINEMA_NAME));
    }
}
