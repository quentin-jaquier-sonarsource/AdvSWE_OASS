package coms.w4156.moviewishlist.repositories;

import coms.w4156.moviewishlist.models.Wishlist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends CrudRepository<Wishlist, Long> {}
