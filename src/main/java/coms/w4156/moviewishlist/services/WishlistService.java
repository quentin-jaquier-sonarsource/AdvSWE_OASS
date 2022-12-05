package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.repositories.WishlistRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishlistService
    extends ServiceForRepository<Long, Wishlist, WishlistRepository> {

    @Autowired
    private ProfileService profileService;

    /**
     * Contstructor to set up dependency injection for the WishlistRepository.
     *
     * @param repository - The repository to inject
     */
    @Autowired
    public WishlistService(final WishlistRepository repository) {
        this.setRepository(repository);
    }

    /**
     * Get all wishlists for a client.
     * @param clientId - The id of the client to get wishlists for
     * @return The list of wishlists for the client
     */
    public List<Wishlist> getAllForClient(final Long clientId) {
        return ((Collection<Wishlist>) this.getRepository().findAll()).stream()
            .filter(w ->
                profileService.findById(w.getProfileId()).get().getClientId() ==
                clientId
            )
            .collect(Collectors.toCollection(ArrayList::new));
    }
}
