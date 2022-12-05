package coms.w4156.moviewishlist.repositories;

import coms.w4156.moviewishlist.models.Profile;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends CrudRepository<Profile, Long> {
    /**
     * Find a profile by its name.
     * @param name - The name of the profile to find
     * @return The profile with the given name
     */
    Optional<Profile> findByName(String name);

    /**
     * Find a profile by its id.
     * @param id - The id of the profile to find
     * @return The profile with the given id
     */
    Optional<Profile> findById(Long id);
}
