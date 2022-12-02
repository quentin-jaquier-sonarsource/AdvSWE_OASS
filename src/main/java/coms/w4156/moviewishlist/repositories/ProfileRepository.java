package coms.w4156.moviewishlist.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coms.w4156.moviewishlist.models.Profile;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {
    public Optional<Profile> findByName(String name);

    public Optional<Profile> findById(Long id);
}
