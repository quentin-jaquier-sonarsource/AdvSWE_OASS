package coms.w4156.moviewishlist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coms.w4156.moviewishlist.Models.Wishlist;
import coms.w4156.moviewishlist.Repositories.WishlistRepository;

@Service
public class WishlistService extends coms.w4156.moviewishlist.services.ServiceForRepository<
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
