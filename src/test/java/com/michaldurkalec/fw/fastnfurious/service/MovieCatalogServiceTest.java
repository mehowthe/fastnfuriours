package com.michaldurkalec.fw.fastnfurious.service;

import com.michaldurkalec.fw.fastnfurious.api.priv.MovieCatalogController.ShowUpdate;
import com.michaldurkalec.fw.fastnfurious.domain.Cinema;
import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.repository.CinemaRepository;
import com.michaldurkalec.fw.fastnfurious.repository.MovieRepository;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.util.collections.Sets.newSet;

@SpringBootTest
public class MovieCatalogServiceTest {

    private static final Long CINEMA_ID = 666L;
    private static final Long CINEMA_ID2 = 999L;
    private static final Date OLD_DATE = Date.from(Instant.now());
    private static final Date NEW_DATE = Date.from(Instant.now().plusMillis(1000));
    private static final Double OLD_PRICE = 100.;
    private static final Double NEW_PRICE = 99.99;
    private static final String MOVIE_ID = "test_id";
    private static final String MOVIE_ID2 = "test_id2";
    private static final String MOVIE_NAME = "F & F";

    private static final Movie TEST_MOVIE1 = new Movie(MOVIE_ID, MOVIE_NAME, emptyList());
    private static final Movie TEST_MOVIE2 = new Movie(MOVIE_ID2, MOVIE_NAME, emptyList());
    private static final MovieShow MOVIE_SHOW1 = MovieShow.builder()
            .id(1L)
            .movie(TEST_MOVIE1)
            .time(OLD_DATE)
            .price(OLD_PRICE)
            .build();
    private final MovieShow MOVIE_SHOW2 = MovieShow.builder()
            .id(2L)
            .movie(TEST_MOVIE2)
            .time(OLD_DATE)
            .price(OLD_PRICE)
            .build();
    private Cinema testCinema;

    @Autowired
    private MovieCatalogService sut;
    @MockBean
    private CinemaRepository cinemaRepository;
    @MockBean
    private MovieRepository movieRepository;

    @BeforeEach
    public void setUp() {
        testCinema = Cinema.builder()
                .id(CINEMA_ID)
                .name("ABC")
                .movieShows(newSet(MOVIE_SHOW1, MOVIE_SHOW2))
                .build();
    }

    @Test
    public void shouldRemoveMovieShows() {
        when(cinemaRepository.findById(CINEMA_ID)).thenReturn(Optional.of(testCinema));
        ShowUpdate removeRequest = new ShowUpdate();
        removeRequest.setShowId(MOVIE_SHOW1.getId());
        removeRequest.setRemove(true);

        Set<MovieShow> result = sut.updateMovieShow(CINEMA_ID, singletonList(removeRequest)).get();
        verify(cinemaRepository).save(any(Cinema.class));
        assertThat(result, contains(MOVIE_SHOW2));
    }

    @Test
    public void shouldUpdateMovieShows() {
        when(cinemaRepository.findById(CINEMA_ID)).thenReturn(Optional.of(testCinema));
        ShowUpdate updateRequest = new ShowUpdate();
        updateRequest.setShowId(MOVIE_SHOW1.getId());
        updateRequest.setTime(NEW_DATE);
        updateRequest.setPrice(NEW_PRICE);

        Set<MovieShow> result = sut.updateMovieShow(CINEMA_ID, singletonList(updateRequest)).get();
        verify(cinemaRepository).save(any(Cinema.class));
        assertThat(result, allOf(
                hasItem(movieShowWithTimeAndPrice(MOVIE_SHOW1.getId(), NEW_DATE, NEW_PRICE)),
                hasItem(movieShowWithTimeAndPrice(MOVIE_SHOW2.getId(), OLD_DATE, OLD_PRICE)))
        );
    }

    @Test
    public void shouldCreateNewMovieShows() {
        Cinema newCiname = Cinema.builder()
                .id(CINEMA_ID2)
                .name("CBA")
                .movieShows(new HashSet<>())
                .build();
        when(cinemaRepository.findById(CINEMA_ID2)).thenReturn(Optional.of(newCiname));
        when(movieRepository.findById(MOVIE_ID)).thenReturn(Optional.of(TEST_MOVIE1));

        ShowUpdate createRequest = new ShowUpdate();
        createRequest.setTime(NEW_DATE);
        createRequest.setPrice(NEW_PRICE);
        createRequest.setMovieId(MOVIE_ID);

        Set<MovieShow> result = sut.updateMovieShow(CINEMA_ID2, singletonList(createRequest)).get();
        verify(cinemaRepository).save(any(Cinema.class));
        MovieShow newMovieShow = result.iterator().next();
        assertThat(newMovieShow.getCinema().getId(), is(CINEMA_ID2));
        assertThat(newMovieShow.getMovie().getMovieId(), is(MOVIE_ID));
    }

    private static Matcher<MovieShow> movieShowWithTimeAndPrice(Long id, Date time, Double price) {
        return new MovieShowMatcher(id, time, price);
    }

    static class MovieShowMatcher extends TypeSafeMatcher<MovieShow> {

        private final Long id;
        private final Date time;
        private final Double price;

        public MovieShowMatcher(Long id, Date time, Double price) {
            this.id = id;
            this.time = time;
            this.price = price;
        }

        @Override
        protected boolean matchesSafely(MovieShow movieShow) {
            return id.equals(movieShow.getId())
                && time.equals(movieShow.getTime())
                && price.equals(movieShow.getPrice());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Expected MovieShow with price ");
            description.appendValue(price);
            description.appendText(" and time ");
            description.appendValue(time);
        }
    }
}
