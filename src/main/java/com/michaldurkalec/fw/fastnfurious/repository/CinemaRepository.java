package com.michaldurkalec.fw.fastnfurious.repository;

import com.michaldurkalec.fw.fastnfurious.domain.Cinema;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CinemaRepository extends CrudRepository<Cinema, Long> {
}
