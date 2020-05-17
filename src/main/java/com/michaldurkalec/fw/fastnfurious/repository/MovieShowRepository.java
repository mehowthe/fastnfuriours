package com.michaldurkalec.fw.fastnfurious.repository;

import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieShowRepository extends PagingAndSortingRepository<MovieShow, Long> {

    @Query("select ms from MovieShow ms where ms.movie.id = ?1")
    List<MovieShow> findByMovie(String movieId);

    @Query("select ms from MovieShow ms where ms.cinema.id = ?1")
    List<MovieShow> findByCinema(Long cinemaId);
}
