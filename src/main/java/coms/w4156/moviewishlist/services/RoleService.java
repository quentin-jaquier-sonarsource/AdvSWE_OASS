package coms.w4156.moviewishlist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coms.w4156.moviewishlist.models.Role;
import coms.w4156.moviewishlist.repositories.RoleRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Service
public class RoleService extends ServiceForRepository<Long, Role, RoleRepository> {

    /**
     * Contstructor to set up dependency injection for the UserRatingRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public RoleService(final RoleRepository repository) {
        this.setRepository(repository);
    }

    public Role findByName(String name) {
        return this.getRepository().findByName(name);
    }

}
