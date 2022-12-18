package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Rating;
import coms.w4156.moviewishlist.models.Wishlist;
import coms.w4156.moviewishlist.repositories.WishlistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WishlistServiceTest {

    @InjectMocks
    private WishlistService wishlistService;

    @Mock
    private WishlistRepository wishlistRepository;

    @Test
    void getAllForClientTest() {
        Client c = Client.builder().id(Long.parseLong("1"))
                                            .email("x@test.com").build();
        Profile p = Profile.builder().client(c).id(Long.parseLong("2")).build();

        Client c2 = Client.builder().id(Long.parseLong("2"))
                .email("x@test.com").build();
        Profile p2 = Profile.builder().client(c2).id(Long.parseLong("1")).build();


        List<Wishlist> wishlists = new ArrayList<>();

        wishlists.add(Wishlist.builder().id(Long.parseLong("3")).profile(p).name("test wishlist 1").build());
        wishlists.add(Wishlist.builder().id(Long.parseLong("4")).profile(p2).name("test wishlist 3").build());

        Mockito
                .when(wishlistRepository.findAll())
                .thenReturn(wishlists);

        List<Wishlist> ret = wishlistService.getAll();
        Assertions.assertNotNull(ret);
        Assertions.assertEquals(ret.size(), wishlists.size());

    }

    @Test
    void getAllForClientFailTest() {
        Client c = Client.builder().id(Long.parseLong("1"))
                .email("x@test.com").build();
        Profile p = Profile.builder().client(c).id(Long.parseLong("2")).build();

        Client c2 = Client.builder().id(Long.parseLong("2"))
                .email("x@test.com").build();
        Profile p2 = Profile.builder().client(c2).id(Long.parseLong("1")).build();


        List<Wishlist> wishlists = new ArrayList<>();

        wishlists.add(Wishlist.builder().id(Long.parseLong("3")).profile(p).name("test wishlist 1").build());
       // wishlists.add(Wishlist.builder().id(Long.parseLong("4")).profile(p2).name("test wishlist 3").build());

        Mockito
                .when(wishlistRepository.findAll())
                .thenReturn(wishlists);

        List<Wishlist> ret = wishlistService.getAll();
        Assertions.assertNotNull(ret);
        Assertions.assertFalse(ret.size() != wishlists.size());

    }
}