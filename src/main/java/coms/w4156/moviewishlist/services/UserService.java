package coms.w4156.moviewishlist.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import coms.w4156.moviewishlist.exceptions.UserAlreadyExistsException;
import coms.w4156.moviewishlist.models.User;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.repository.UserRepository;

@Service
public class UserService extends ServiceForRepository<
    Long,
    User,
    UserRepository
> implements UserDetailsService {
    /**
     * Constructor to set up dependency injection for the UserRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public UserService(final UserRepository repository) {
        this.setRepository(repository);
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    private BCryptPasswordEncoder passwordEncoder;

    // This should only be used for authentication, as it returns a spring.security.UserDetails
    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.getRepository().findByUsername(username);

        if (user.isPresent()) {
            // TODO: make a builder to create a UserDetails from a User
            return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getEncodedPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User with username " + username + " not found.");
        }
    }

    public UserDetails createUserAndReturnDetails(String email, String username, String password) throws UserAlreadyExistsException {
        Optional<User> alreadyExistingUserEmail = this.getRepository().findByEmail(email);
        Optional<User> alreadyExistingUserUsername = this.getRepository().findByUsername(username);

        if (alreadyExistingUserEmail.isPresent() || alreadyExistingUserUsername.isPresent()) {
            throw new UserAlreadyExistsException("A user with email " + email + "and username " + username + " already exists");
        }

        final String encodedPassword = passwordEncoder.encode(password);

        User user = new User(email, username, encodedPassword, new ArrayList<Wishlist>());
        this.getRepository().save(user);

        UserDetails userDetails = this.loadUserByUsername(user.getUsername());

        return userDetails;
    }
}
