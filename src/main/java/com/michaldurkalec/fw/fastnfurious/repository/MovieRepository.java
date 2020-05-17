package com.michaldurkalec.fw.fastnfurious.repository;

import com.michaldurkalec.fw.fastnfurious.domain.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, String> {
}
