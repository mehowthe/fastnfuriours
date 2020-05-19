package com.michaldurkalec.fw.fastnfurious.service;

import com.michaldurkalec.fw.fastnfurious.api.priv.MovieCatalogController.ShowUpdate;
import com.michaldurkalec.fw.fastnfurious.domain.Cinema;
import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import com.michaldurkalec.fw.fastnfurious.repository.CinemaRepository;
import com.michaldurkalec.fw.fastnfurious.repository.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.Objects.isNull;

@Component
public class MovieCatalogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MovieCatalogService.class);

    @Autowired
    private CinemaRepository cinemaRepository;
    @Autowired
    private MovieRepository movieRepository;

    public Optional<Set<MovieShow>> updateMovieShow(Long cinemaId, List<ShowUpdate> updates) {
        Optional<Cinema> maybeCinema = cinemaRepository.findById(cinemaId);
        if (!maybeCinema.isPresent()) {
            return Optional.empty();
        }
        Cinema cinema = maybeCinema.get();

        Set<MovieShow> movieShows = cinema.getMovieShows();
        processRemoveUpdates(updates, movieShows);
        processAlterUpdates(updates, movieShows);
        processCreateUpdates(updates, cinema, movieShows);

        cinemaRepository.save(cinema);
        return Optional.of(cinema.getMovieShows());

    }

    private void processCreateUpdates(List<ShowUpdate> updates, Cinema cinema, Set<MovieShow> movieShows) {
        updates.stream()
                .filter(MovieCatalogService::isCreate)
                .map(createRequest -> createNew(cinema, createRequest))
                .forEach(movieShows::add);
    }

    private void processAlterUpdates(List<ShowUpdate> updates, Set<MovieShow> movieShows) {
        updates.stream()
                .filter(MovieCatalogService::isUpdate)
                .forEach(updateRequest -> movieShows.stream()
                            .filter(show -> updateRequest.getShowId().equals(show.getId()))
                            .findFirst()
                            .ifPresent(updateExisting(updateRequest)));
    }

    private void processRemoveUpdates(List<ShowUpdate> updates, Set<MovieShow> movieShows) {
        updates.stream()
                .filter(ShowUpdate::isRemove)
                .forEach(removeRequest -> movieShows.removeIf(show -> removeRequest.getShowId().equals(show.getId())));
    }

    private static boolean isUpdate(ShowUpdate showUpdate) {
        return !showUpdate.isRemove() && !isNull(showUpdate.getShowId());
    }

    private static boolean isCreate(ShowUpdate showUpdate) {
        return !showUpdate.isRemove() && !isUpdate(showUpdate);
    }

    private Consumer<MovieShow> updateExisting(ShowUpdate updateRequest) {
        return show -> {
            if (!isNull(updateRequest.getTime())) {
                show.setTime(updateRequest.getTime());
            }
            if (!isNull(updateRequest.getPrice())) {
                show.setPrice(updateRequest.getPrice());
            }
        };
    }

    private MovieShow createNew(Cinema cinema, ShowUpdate createRequest) {
        Optional<Movie> movie = movieRepository.findById(createRequest.getMovieId());
        if (!movie.isPresent()) {
            LOGGER.warn("Movie not found, cannot process request : {}", createRequest);
            return null;
        }
        return MovieShow.builder()
                .cinema(cinema)
                .movie(movie.get())
                .time(createRequest.getTime())
                .price(createRequest.getPrice())
                .build();
    }
}
