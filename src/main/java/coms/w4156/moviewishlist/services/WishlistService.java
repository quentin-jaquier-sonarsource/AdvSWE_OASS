package coms.w4156.moviewishlist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.repository.WishlistRepository;

@Service
public class WishlistService {

    private final WishlistRepository repo;

    @Autowired
    public WishlistService(final WishlistRepository repo) {
        this.repo = repo;
    }

    public Wishlist findById(long id) {
        return this.repo.findById(id);
    }
    
}
