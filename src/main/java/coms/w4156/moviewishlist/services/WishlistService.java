package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.repositories.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistService
    extends ServiceForRepository<Long, Wishlist, WishlistRepository> {

    /**
     * Contstructor to set up dependency injection for the WishlistRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public WishlistService(final WishlistRepository repository) {
        this.setRepository(repository);
    }
}
