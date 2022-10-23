package coms.w4156.moviewishlist.services;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coms.w4156.moviewishlist.models.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import coms.w4156.moviewishlist.repository.UserRepository;

@Service
public class UserService extends ServiceForRepository<Long, User, UserRepository> implements UserDetailsService {
    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    private BCryptPasswordEncoder passwordEncoder;

    // This should only be used for authentication, as it returns a spring.security.UserDetails
    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.repository.findByUsername(username);

        if (user.isPresent()) {
            // TODO: make a builder to create a UserDetails from a User
            return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getEncryptedPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User with username " + username + " not found.");
        }
    }

    public UserDetails createUserAndReturnDetails(String email, String username, String password) {
        // TODO: check if username already exists
        System.out.println("createUserAndReturn");
        final String encryptedPassword = passwordEncoder.encode(password);
        
        User user = new User(email, username, encryptedPassword);
        repository.save(user);

        System.out.println("loadUsername");

        UserDetails userDetails = this.loadUserByUsername(user.getUsername());
        
        return userDetails;
    }
}
