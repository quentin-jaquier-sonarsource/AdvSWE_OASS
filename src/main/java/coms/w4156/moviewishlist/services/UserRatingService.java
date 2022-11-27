package coms.w4156.moviewishlist.services;


import coms.w4156.moviewishlist.models.Ratings;
import coms.w4156.moviewishlist.repositories.UserRatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRatingService extends ServiceForRepository<
        Long,
        Ratings,
        UserRatingRepository
    > {
    /**
     * Contstructor to set up dependency injection for the UserRatingRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public UserRatingService(final UserRatingRepository repository) {
        this.setRepository(repository);
    }

}
