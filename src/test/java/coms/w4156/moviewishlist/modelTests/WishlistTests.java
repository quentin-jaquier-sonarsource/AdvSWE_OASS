package coms.w4156.moviewishlist.modelTests;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Movie;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Wishlist;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class WishlistTests {

    @Test
    void testNameProfConstructor() {

        Profile profile = new Profile();

        String name = "name";
        Wishlist wishlist1 = new Wishlist(name, profile);
        Wishlist wishlist = new Wishlist(name, profile);

        Assertions.assertEquals(wishlist, wishlist1);
    }

    @Test
    void testGetProfId() {

        long id = 42L;
        Profile profile = Profile.builder().id(id).build();

        String name = "name";
        Wishlist wishlist1 = new Wishlist(name, profile);
        Wishlist wishlist = new Wishlist(name, profile);

        Assertions.assertEquals(id, wishlist.getProfileId());
    }

    @Test
    void testGetClientId() {

        long profId = 42L;
        long clientId = 1L;
        Client client = Client.builder().id(clientId).build();
        Profile profile = Profile.builder().id(profId).client(client).build();

        String name = "name";

        Wishlist wishlist = new Wishlist(name, profile);

        Assertions.assertEquals(clientId, wishlist.getClientId());
    }

    @Test
    void testMovieIds() {

        String name = "name";
        Profile profile = new Profile();

        List<Long> ids = List.of(1L, 2L, 3L, 4L, 5L);
        List<Movie> movies = new ArrayList<>();



        for (Long id : ids) {
            movies.add(Movie.builder().id(id).build());
        }

        Wishlist wishlist = new Wishlist(name, profile);
        wishlist.setMovies(movies);
        List<Long> ret = wishlist.getMovieIds();

        Assertions.assertTrue(ids.size() == ret.size() && ids.containsAll(ret) && ret.containsAll(ids));
    }
}
