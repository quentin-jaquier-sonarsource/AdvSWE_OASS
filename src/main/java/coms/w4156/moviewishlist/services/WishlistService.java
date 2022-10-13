package coms.w4156.moviewishlist.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.repository.WishlistRepository;

@Service
public class WishlistService {

    private final WishlistRepository wlRepository;

    @Autowired
    public WishlistService(final WishlistRepository repo) {
        this.wlRepository = repo;
    }

    public Optional<Wishlist> findById(long id) {
        return this.wlRepository.findById(id);
    }

    public List<Wishlist> getAll() {
        List<Wishlist> wlList = new ArrayList<Wishlist>();
        
        this.wlRepository.findAll().forEach(wlList::add);
        return wlList;
    }

    public Wishlist create(String name, Long userId) {
        return this.wlRepository.save(new Wishlist(name, userId));
    }

    public void deleteAll() {
        this.wlRepository.deleteAll();
    }

    public void deleteById(Long id) {
        this.wlRepository.deleteById(id);
    }
    
}
