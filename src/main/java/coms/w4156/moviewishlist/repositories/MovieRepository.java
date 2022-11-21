package coms.w4156.moviewishlist.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coms.w4156.moviewishlist.Models.Movie;

@Repository
public interface MovieRepository extends CrudRepository<Movie, Long> {

}
