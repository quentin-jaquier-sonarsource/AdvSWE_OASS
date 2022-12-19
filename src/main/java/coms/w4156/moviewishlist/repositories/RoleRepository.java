package coms.w4156.moviewishlist.repositories;

import coms.w4156.moviewishlist.models.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Find a role by its name.
     * @param name - The name of the role to find
     * @return The role with the given name
     */
    Role findByName(String name);
}