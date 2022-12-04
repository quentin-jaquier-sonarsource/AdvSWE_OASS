package coms.w4156.moviewishlist.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.repositories.ProfileRepository;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService
    extends ServiceForRepository<Long, Profile, ProfileRepository> {

    /**
     * Constructor to set up dependency injection for the ProfileRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public ProfileService(final ProfileRepository repository) {
        this.setRepository(repository);
    }

    /**
     * Find a profile by its name.
     * @param name
     * @return the profile
     * @throws Error
     */
    public Optional<Profile> findByName(final String name) throws Error {
        Optional<Profile> profile = this.getRepository().findByName(name);
        return profile;
    }

    public List<Profile> getAllForClient(Long clientId) {
        return ((Collection<Profile>) this.getRepository().findAll()).stream()
            .filter(p -> p.getClientId() == clientId)
            .collect(Collectors.toCollection(ArrayList:: new));
    }

    public Optional<Profile> findById(final Long id) throws Error {
        Optional<Profile> profile = this.getRepository().findById(id);
        return profile;
    }
}
