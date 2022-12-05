package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.repositories.RatingRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Getter
@Setter
@Service
public class RatingService
    extends ServiceForRepository<Long, Rating, RatingRepository> {

    /**
     * Contstructor to set up dependency injection for the UserRatingRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public RatingService(final RatingRepository repository) {
        this.setRepository(repository);
    }

    /**
     * Find all ratings for a client.
     *
     * @param clientId - The id of the client to find ratings for
     * @return The list of ratings for the client
     */
    public List<Rating> getAllForClient(final Long clientId) {
        return this.getRepository()
            .findAll()
            .stream()
            .filter(r -> r.getClientId() == clientId)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Find all ratings for a profiles.
     *
     * @param profileId - The id of the profile to find ratings for
     * @return The list of ratings for the profile
     */
    public List<Rating> getAllForProfileId(final Long profileId) {
        return this.getRepository()
            .findAll()
            .stream()
            .filter(r -> r.getProfileId() == profileId)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Find all ratings for a movie and a client.
     *
     * @param movieId - The id of the movie to find ratings for
     * @param clientId - The id of the client to find ratings for
     * @return The list of ratings for the movie
     */
    public List<Rating> getByMovieIdForClient(
        final Long movieId,
        final Long clientId
    ) {
        return this.getRepository()
            .findAll()
            .stream()
            .filter(r ->
                r.getMovieId() == movieId && r.getClientId() == clientId
            )
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
