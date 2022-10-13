package coms.w4156.moviewishlist.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coms.w4156.moviewishlist.models.Wishlist;

@Repository
public interface WishlistRepository extends CrudRepository<Wishlist, Long> {

}
