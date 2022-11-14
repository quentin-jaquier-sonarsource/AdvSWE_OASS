package coms.w4156.moviewishlist.repository;

import coms.w4156.moviewishlist.models.Movie;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {}
