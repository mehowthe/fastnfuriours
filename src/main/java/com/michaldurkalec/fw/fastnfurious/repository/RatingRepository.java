package com.michaldurkalec.fw.fastnfurious.repository;

import com.michaldurkalec.fw.fastnfurious.domain.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RatingRepository extends CrudRepository<Rating, Long> {

    @Query("select r from Rating r where r.movie.id = ?1 and r.userIp = ?2")
    Optional<Rating> findOneByMovieIdAndUserIp(String movieId, String userIp);
}
