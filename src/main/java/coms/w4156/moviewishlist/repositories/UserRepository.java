package coms.w4156.moviewishlist.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coms.w4156.moviewishlist.Models.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

}
