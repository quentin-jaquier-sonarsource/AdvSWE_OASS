package coms.w4156.moviewishlist.repositories;

import coms.w4156.moviewishlist.models.Client;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    /**
     * Find a client by their email.
     * @param email - The email of the client to find
     * @return The client with the given email
     */
    Optional<Client> findByEmail(String email);
}
