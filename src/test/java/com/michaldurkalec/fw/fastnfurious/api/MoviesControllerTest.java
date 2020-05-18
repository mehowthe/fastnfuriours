package com.michaldurkalec.fw.fastnfurious.api;

import com.michaldurkalec.fw.fastnfurious.domain.Cinema;
import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.domain.Rating;
import com.michaldurkalec.fw.fastnfurious.domain.dto.MovieDetails;
import com.michaldurkalec.fw.fastnfurious.domain.dto.OMDbMovieDetails;
import com.michaldurkalec.fw.fastnfurious.repository.CinemaRepository;
import com.michaldurkalec.fw.fastnfurious.repository.MovieRepository;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static java.util.Collections.emptyList;
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
    private static final Movie TEST_MOVIE = new Movie(EXAMPLE_ID, EXAMPLE_TITLE, emptyList());
    private static final String TEST_CINEMA_NAME = "Cool cinema";
    private static final long TEST_CINEMA_ID = 666L;
    private static final String TEST_MOVIESHOW_TIME = "18-05-2020 08:00:00";
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private OMDbClient omdbClient;
    @MockBean
    private MovieShowRepository movieShowRepository;
    @MockBean
    private CinemaRepository cinemaRepository;
    @MockBean
    private MovieRepository movieRepository;

    @BeforeEach
    public void setUp() {
        this.basePath = "http://localhost:" + port + "/movies";
    }

    @Test
    public void shouldListMovies() {
        when(movieRepository.findAll()).thenReturn(singletonList(TEST_MOVIE));
        ResponseEntity<Movie[]> result = restTemplate
                .getForEntity(basePath + "/", Movie[].class);
        assertThat(result.getStatusCode(), is(OK));

        Movie movie = requireNonNull(result.getBody())[0];
        assertThat(movie, is(TEST_MOVIE));
    }

    @Test
    public void shouldReturnMovieDetails() {

        OMDbMovieDetails expectedResult = new OMDbMovieDetails();
        expectedResult.setTitle(EXAMPLE_TITLE);
        when(omdbClient.fetchMovieDetails(EXAMPLE_ID)).thenReturn(expectedResult);

        ResponseEntity<MovieDetails> result = restTemplate
                .getForEntity(basePath + "/" + EXAMPLE_ID, MovieDetails.class);

        assertThat(result.getStatusCode(), is(OK));
        assertThat(requireNonNull(result.getBody()).getName(), is(EXAMPLE_TITLE));
    }

    @Test
    public void shouldReturnExtendedMovieDetails() {

        OMDbMovieDetails expectedResult = new OMDbMovieDetails();
        expectedResult.setTitle(EXAMPLE_TITLE);
        TEST_MOVIE.setRatings(singletonList(Rating.builder().score(50.f).build()));

        when(omdbClient.fetchMovieDetails(EXAMPLE_ID)).thenReturn(expectedResult);
        when(movieRepository.findById(EXAMPLE_ID)).thenReturn(Optional.of(TEST_MOVIE));

        ResponseEntity<MovieDetails> result = restTemplate
                .getForEntity(basePath + "/" + EXAMPLE_ID + "?extended=true", MovieDetails.class);

        assertThat(result.getStatusCode(), is(OK));
        assertThat(requireNonNull(result.getBody()).getMovieId(), is(EXAMPLE_ID));
        assertThat(requireNonNull(result.getBody()).getPrivateRating(), is(50.f));
    }

    @Test
    public void shouldAcceptNewRate() {

    }

    @Test
    public void shouldRejectRateForExistingIp() {

    }


    @Test
    public void shouldReturnMovieShowsByMovie() throws Exception {

        Date movieDate = DATE_FORMAT.parse(TEST_MOVIESHOW_TIME);
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
        assertThat(DATE_FORMAT.format(show.getTime()), is(TEST_MOVIESHOW_TIME));
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
