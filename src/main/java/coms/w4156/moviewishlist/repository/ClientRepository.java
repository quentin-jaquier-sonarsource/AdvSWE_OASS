package coms.w4156.moviewishlist.repository;

import coms.w4156.moviewishlist.models.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends CrudRepository<Client, Long> {}
