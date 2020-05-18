package com.michaldurkalec.fw.fastnfurious.domain;

import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class MovieTest {

    @Test
    public void shouldReturnNullIfNoScores() {
        Movie movie = new Movie("id", "title", emptyList());
        assertThat(movie.getAvgScore(), is(nullValue()));
    }

    @Test
    public void shouldCalculateAvg() {
        Rating rating1 = Rating.builder().score(50.f).build();
        Rating rating2 = Rating.builder().score(100.f).build();

        Movie movie = new Movie("id", "title", asList(rating1, rating2));

        assertThat(movie.getAvgScore(), is(75.f));
    }
}
