package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.models.User;
import coms.w4156.moviewishlist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService
    extends ServiceForRepository<String, User, UserRepository> {

    /**
     * Contstructor to set up dependency injection for the UserRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public UserService(final UserRepository repository) {
        this.setRepository(repository);
    }
}
