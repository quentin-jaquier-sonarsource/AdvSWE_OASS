package coms.w4156.moviewishlist.repositories;

import coms.w4156.moviewishlist.models.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RatingRepository extends JpaRepository<Ratings, Long> {
}
