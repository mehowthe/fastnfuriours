package com.michaldurkalec.fw.fastnfurious.api;

import com.michaldurkalec.fw.fastnfurious.domain.dto.MovieDetails;
import com.michaldurkalec.fw.fastnfurious.domain.dto.OMDbMovieDetails;
import com.michaldurkalec.fw.fastnfurious.service.OMDbClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MoviesControllerTest {

    @LocalServerPort
    private int port;

    private static final String EXAMPLE_ID="tt0232500";
    private static final String EXAMPLE_TITLE="The Fast and the Furious";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private OMDbClient omdbClient;

    @Test
    public void shouldReturnMovieDetails() {

        OMDbMovieDetails expectedResult = new OMDbMovieDetails();
        expectedResult.setTitle(EXAMPLE_TITLE);
        given(omdbClient.fetchMovieDetails(EXAMPLE_ID)).willReturn(expectedResult);

        ResponseEntity<MovieDetails> result = this.restTemplate
                .getForEntity("http://localhost:" + port + "/movies/details?id=" + EXAMPLE_ID, MovieDetails.class);
        assertThat(result.getStatusCode(), is(OK));
        assertThat(result.getBody().getName(), is(EXAMPLE_TITLE));
    }
}
