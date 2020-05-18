package com.michaldurkalec.fw.fastnfurious.api;

import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.Rating;
import com.michaldurkalec.fw.fastnfurious.repository.MovieRepository;
import com.michaldurkalec.fw.fastnfurious.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Optional.empty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoviesRatingControllerTest {

    private static final String EXAMPLE_ID="tt0232500";
    private static final String EXAMPLE_TITLE="The Fast and the Furious";
    private static final Movie TEST_MOVIE = new Movie(EXAMPLE_ID, EXAMPLE_TITLE, emptyList());
    private static final String TEST_USER_IP = "127.0.0.1";
    private static final String TEST_RATING_BODY = "{\"score\": \"50\"}";

    @LocalServerPort
    private int port;

    private String basePath;

    @Autowired
    private TestRestTemplate restTemplate;
    private final HttpHeaders headers = new HttpHeaders();

    @MockBean
    private RatingRepository ratingRepository;
    @MockBean
    private MovieRepository movieRepository;

    @BeforeEach
    public void setUp() {
        this.basePath = "http://localhost:" + port + "/movies";
        this.headers.setContentType(APPLICATION_JSON);
    }

    @Test
    public void shouldReturnNotFoundForWrongMovie() {
        when(movieRepository.findById(EXAMPLE_ID)).thenReturn(Optional.empty());
        HttpEntity<Object> request = new HttpEntity<>(TEST_RATING_BODY, headers);
        ResponseEntity<?> result = restTemplate.postForEntity(basePath + "/" + EXAMPLE_ID + "/rate", request, String.class);

        verifyNoInteractions(ratingRepository);
        assertThat(result.getStatusCode(), is(NOT_FOUND));
    }

    @Test
    public void shouldAcceptNewRate() {

        when(movieRepository.findById(EXAMPLE_ID)).thenReturn(Optional.of(TEST_MOVIE));
        when(ratingRepository.findOneByMovieIdAndUserIp(EXAMPLE_ID, TEST_USER_IP)).thenReturn(empty());

        HttpEntity<Object> request = new HttpEntity<>(TEST_RATING_BODY, headers);
        ResponseEntity<?> result = restTemplate.postForEntity(basePath + "/" + EXAMPLE_ID + "/rate", request, String.class);

        verify(ratingRepository).save(any(Rating.class));
        assertThat(result.getStatusCode(), is(CREATED));
    }

    @Test
    public void shouldRejectRateForExistingIp() {

        when(movieRepository.findById(EXAMPLE_ID)).thenReturn(Optional.of(TEST_MOVIE));
        when(ratingRepository.findOneByMovieIdAndUserIp(eq(EXAMPLE_ID), any(String.class))).thenReturn(Optional.of(new Rating()));

        HttpEntity<Object> request = new HttpEntity<>(TEST_RATING_BODY, headers);
        ResponseEntity<?> result = restTemplate.postForEntity(basePath + "/" + EXAMPLE_ID + "/rate", request, String.class);

        assertThat(result.getStatusCode(), is(TOO_MANY_REQUESTS));
    }
}
