package com.michaldurkalec.fw.fastnfurious.repository;

import com.michaldurkalec.fw.fastnfurious.domain.MovieShow;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieShowRepository extends PagingAndSortingRepository<MovieShow, Long> {
}
