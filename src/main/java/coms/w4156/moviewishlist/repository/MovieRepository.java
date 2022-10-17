package coms.w4156.moviewishlist.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coms.w4156.moviewishlist.models.Movie;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

}
