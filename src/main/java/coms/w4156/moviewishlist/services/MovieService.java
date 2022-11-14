package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieService
    extends ServiceForRepository<Long, Movie, MovieRepository> {

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
