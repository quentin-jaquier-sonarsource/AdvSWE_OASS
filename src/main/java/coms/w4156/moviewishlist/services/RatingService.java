package coms.w4156.moviewishlist.services;


import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.repositories.RatingRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@NoArgsConstructor
@Getter
@Setter
@Service
public class RatingService extends ServiceForRepository<
        Long,
        Rating,
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

    public List<Rating> getAllForClient(Long clientId) {
        return this.getRepository().findAll().stream()
            .filter(r -> r.getClientId() == clientId)
            .collect(Collectors.toCollection(ArrayList:: new));
    }

    public List<Rating> getAllForProfileId(Long profileId) {
        return this.getRepository().findAll().stream()
                .filter(r -> r.getProfileId() == profileId)
                .collect(Collectors.toCollection(ArrayList:: new));
    }

    public List<Rating> getByMovieIdForClient(Long movieId, Long clientId) {
        return this.getRepository().findAll().stream()
        .filter(r -> r.getMovieId() == movieId && r.getClientId() == clientId)
        .collect(Collectors.toCollection(ArrayList:: new));
    }

}
