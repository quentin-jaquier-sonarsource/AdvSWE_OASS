package coms.w4156.moviewishlist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.repository.WishlistRepository;

@Service
public class WishlistService extends ServiceForRepository<
        Long,
        Wishlist,
        WishlistRepository
> {
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
