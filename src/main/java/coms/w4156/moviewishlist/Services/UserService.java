package coms.w4156.moviewishlist.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

// import coms.w4156.moviewishlist.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

    // private final UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        // this.userRepository = repo; 
    }

    @Override public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // return this.userRepository.findByUsername(username);

        System.out.println("On est là: " + username);

        if ("adrien".equals(username)) {
            System.out.println("on renvoit adrien");
            return new User("adrien", "test123", new ArrayList<>());
        } else {
            System.out.println("throwwwwwsss");
            throw new UsernameNotFoundException("User with username " + username + " not found.");
        }
        
    }

    public User createUser(String username, String password) {
        // TODO: check if username already exists
        final String encryptedPassword = passwordEncoder.encode(password);

        User user = new User(username, encryptedPassword, new ArrayList<>());

        // TODO: create and use userRepository
        // final User createdUser = userRepository.save(user);
        final User createdUser = user;
        
        return createdUser;
    }
        
}
