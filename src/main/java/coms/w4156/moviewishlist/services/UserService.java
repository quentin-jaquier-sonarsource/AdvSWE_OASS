package coms.w4156.moviewishlist.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coms.w4156.moviewishlist.models.User;
import coms.w4156.moviewishlist.repository.UserRepository;

@Service
public class UserService extends ServiceForRepository<
    Long,
    User,
    UserRepository
> {
    /**
     * Constructor to set up dependency injection for the UserRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public UserService(final UserRepository repository) {
        this.setRepository(repository);
    }

    public Optional<User> findByEmail(String email) throws Error {
        Optional<User> user = this.getRepository().findByEmail(email);
        return user;
    }
}
