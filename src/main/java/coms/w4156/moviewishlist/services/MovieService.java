package coms.w4156.moviewishlist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coms.w4156.moviewishlist.Models.Movie;
import coms.w4156.moviewishlist.Repositories.MovieRepository;

@Service
public class MovieService extends coms.w4156.moviewishlist.services.ServiceForRepository<
    Long,
    Movie,
    MovieRepository
> {
    /**
     * Contstructor to set up dependency injection for the MovieRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public MovieService(final MovieRepository repository) {
        this.setRepository(repository);
    }
}
