package coms.w4156.moviewishlist.repositories;

import coms.w4156.moviewishlist.models.MovieRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRatingRepository extends JpaRepository<MovieRating, Long> {
}
