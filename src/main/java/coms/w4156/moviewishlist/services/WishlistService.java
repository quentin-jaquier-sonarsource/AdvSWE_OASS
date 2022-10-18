package coms.w4156.moviewishlist.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.repository.WishlistRepository;

@Service
public class WishlistService extends ServiceForRepository<Long, Wishlist, WishlistRepository> {
    @Autowired
    public WishlistService(WishlistRepository repository) {
        this.repository = repository;
    }
}
