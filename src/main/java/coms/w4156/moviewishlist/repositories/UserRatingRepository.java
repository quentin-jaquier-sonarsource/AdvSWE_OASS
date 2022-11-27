package coms.w4156.moviewishlist.repositories;

import coms.w4156.moviewishlist.models.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRatingRepository extends JpaRepository<Ratings, Long> {
}
