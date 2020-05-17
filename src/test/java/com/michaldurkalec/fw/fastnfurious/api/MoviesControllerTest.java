package com.michaldurkalec.fw.fastnfurious.api;

import com.michaldurkalec.fw.fastnfurious.domain.Cinema;
import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.domain.dto.MovieDetails;
import com.michaldurkalec.fw.fastnfurious.domain.dto.OMDbMovieDetails;
import com.michaldurkalec.fw.fastnfurious.repository.CinemaRepository;
import com.michaldurkalec.fw.fastnfurious.repository.MovieShowRepository;
import com.michaldurkalec.fw.fastnfurious.service.OMDbClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.Date;

import static java.util.Collections.singletonList;
import static java.util.Objects.requireNonNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoviesControllerTest {

    @LocalServerPort
    private int port;

    private String basePath;

    private static final String EXAMPLE_ID="tt0232500";
    private static final String EXAMPLE_TITLE="The Fast and the Furious";
    private static final Movie TEST_MOVIE = new Movie(EXAMPLE_ID, EXAMPLE_TITLE);
    private static final String TEST_CINEMA_NAME = "Cool cinema";
    private static final long TEST_CINEMA_ID = 666L;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private OMDbClient omdbClient;

    @MockBean
    private MovieShowRepository movieShowRepository;
    @MockBean
    private CinemaRepository cinemaRepository;

    @BeforeEach
    public void setUp() {
        this.basePath = "http://localhost:" + port + "/movies";
    }

    @Test
    public void shouldReturnMovieDetails() {

        OMDbMovieDetails expectedResult = new OMDbMovieDetails();
        expectedResult.setTitle(EXAMPLE_TITLE);
        when(omdbClient.fetchMovieDetails(EXAMPLE_ID)).thenReturn(expectedResult);

        ResponseEntity<MovieDetails> result = restTemplate
                .getForEntity(basePath + "/details?id=" + EXAMPLE_ID, MovieDetails.class);
        assertThat(result.getStatusCode(), is(OK));
        assertThat(requireNonNull(result.getBody()).getName(), is(EXAMPLE_TITLE));
    }

    @Test
    public void shouldReturnMovieShowsByMovie() {

        Date movieDate = Date.from(Instant.now());
        MovieShow movieShow = MovieShow.builder()
                .movie(TEST_MOVIE)
                .time(movieDate)
                .build();
        when(movieShowRepository.findByMovie(EXAMPLE_ID)).thenReturn(singletonList(movieShow));

        ResponseEntity<MovieShow[]> result = restTemplate
                .getForEntity(basePath + "/shows?id=" + EXAMPLE_ID, MovieShow[].class);
        assertThat(result.getStatusCode(), is(OK));
        MovieShow show = requireNonNull(result.getBody())[0];
        assertThat(show.getMovie().getMovieId(), is(EXAMPLE_ID));
        assertThat(show.getTime(), is(movieDate));
    }

    @Test
    public void shouldListCinemas() {
        Cinema testCinema = Cinema.builder()
                .id(TEST_CINEMA_ID)
                .name(TEST_CINEMA_NAME)
                .build();
        when(cinemaRepository.findAll()).thenReturn(singletonList(testCinema));

        ResponseEntity<Cinema[]> result = restTemplate
                .getForEntity(basePath + "/cinemas", Cinema[].class);

        assertThat(result.getStatusCode(), is(OK));
        Cinema cinema = requireNonNull(result.getBody())[0];
        assertThat(cinema.getName(), is(TEST_CINEMA_NAME));
        assertThat(cinema.getId(), is(TEST_CINEMA_ID));
    }

    @Test
    public void shouldReturnMovieShowsByCinema() {

        MovieShow movieShow = MovieShow.builder()
                .movie(TEST_MOVIE)
                .cinema(Cinema.builder()
                        .id(TEST_CINEMA_ID)
                        .name(TEST_CINEMA_NAME)
                        .build())
                .build();
        when(movieShowRepository.findByCinema(TEST_CINEMA_ID)).thenReturn(singletonList(movieShow));

        ResponseEntity<MovieShow[]> result = restTemplate
                .getForEntity(basePath + "/cinemas/program?id=" + TEST_CINEMA_ID, MovieShow[].class);

        assertThat(result.getStatusCode(), is(OK));
        MovieShow show = requireNonNull(result.getBody())[0];
        assertThat(show.getCinema().getName(), is(TEST_CINEMA_NAME));

    }
}
