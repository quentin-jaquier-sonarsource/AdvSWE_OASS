package coms.w4156.moviewishlist.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coms.w4156.moviewishlist.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    public Optional<User> findByUsername(String username);

    public Optional<User> findFirstByEmail(String email);

    public Optional<User> findById(Long id);
}
