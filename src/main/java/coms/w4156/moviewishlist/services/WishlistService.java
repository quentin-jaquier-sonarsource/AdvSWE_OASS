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

    @Autowired
    private WishlistRepository wlRepository;

    public Optional<Wishlist> findById(Long id) {
        return this.wlRepository.findById(id);
    }

    public List<Wishlist> getAll() {
        List<Wishlist> wlList = new ArrayList<Wishlist>();

        this.wlRepository.findAll().forEach(wlList::add);
        return wlList;
    }
    
    public Wishlist create(Wishlist wishlist) {
        return this.wlRepository.save(wishlist);
    }

    public Wishlist update(Wishlist wishlist) {
        return this.wlRepository.save(wishlist);
    }

    public Wishlist create(String name, Long userId) {
        return this.wlRepository.save(new Wishlist(name, userId));
    }

    public void deleteAll() {
        this.wlRepository.deleteAll();
    }

    public Optional<Wishlist> deleteById(Long id) {
        Optional<Wishlist> wishlist = this.wlRepository.findById(id);

        if (wishlist.isPresent()) {
            this.wlRepository.deleteById(id);
        }

        return wishlist;
    }
    
}
