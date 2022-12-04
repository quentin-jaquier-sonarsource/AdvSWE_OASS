package coms.w4156.moviewishlist.repositories;

import coms.w4156.moviewishlist.models.Client;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {
    public Optional<Client> findByEmail(String email);
}
