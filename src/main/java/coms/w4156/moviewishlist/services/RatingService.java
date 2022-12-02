package coms.w4156.moviewishlist.services;


import coms.w4156.moviewishlist.models.Ratings;
import coms.w4156.moviewishlist.repositories.RatingRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Getter
@Setter
@Service
public class RatingService extends ServiceForRepository<
        Long,
        Ratings,
        RatingRepository
    > {
    /**
     * Contstructor to set up dependency injection for the UserRatingRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public RatingService(final RatingRepository repository) {
        this.setRepository(repository);
    }

}
