package coms.w4156.moviewishlist.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coms.w4156.moviewishlist.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
