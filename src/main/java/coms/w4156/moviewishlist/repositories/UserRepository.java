package coms.w4156.moviewishlist.repository;

import coms.w4156.moviewishlist.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {}
