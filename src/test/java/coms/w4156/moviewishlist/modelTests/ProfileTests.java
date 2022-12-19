package coms.w4156.moviewishlist.modelTests;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.models.Wishlist;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ProfileTests {

    @Test
    void testToString() {
        Profile profile = new Profile();

        Assertions.assertEquals(
            "Profile(id=null, name=null, wishlists=null, client=null, ratings=null)",
            profile.toString()
        );
    }

    @Test
    void testEquals() {
        Profile profile = new Profile();
        Profile profile1 = new Profile();

        Assertions.assertEquals(profile1, profile);
    }

    @Test
    void testGetWishlists() {
        Profile profile = new Profile();

        Assertions.assertEquals(null, profile.getWishlists());
    }

    @Test
    void testNameConstructor() {
        String group_a = "Group A";
        Profile profile = new Profile(group_a);

        Assertions.assertEquals(group_a, profile.getName());
    }

    @Test
    void testGetRatings() {
        Profile profile = new Profile();

        Assertions.assertEquals(null, profile.getRatings());
    }

    @Test
    void testNameWishlistsClientConstructor() {
        String name = "Group A";
        Wishlist wishlistA = new Wishlist();
        Wishlist wishlistB = new Wishlist();
        List<Wishlist> wishlists = List.of(wishlistA, wishlistB);
        Client client = new Client();

        Profile profile = new Profile(name, wishlists, client);

        Assertions.assertEquals(wishlists, profile.getWishlists());
        Assertions.assertEquals(name, profile.getName());
        Assertions.assertEquals(client, profile.getClient());
    }

    @Test
    void testGetWishlistIds() {
        String name = "Group A";
        Wishlist wishlistA = new Wishlist();
        Wishlist wishlistB = new Wishlist();

        wishlistA.setId(45L);
        wishlistB.setId(42L);

        List<Long> ids = List.of(42L, 45L);

        List<Wishlist> wishlists = List.of(wishlistA, wishlistB);
        Client client = new Client();

        Profile profile = new Profile(name, wishlists, client);

        List<Long> ret = profile.getWishlistIds();

        Assertions.assertTrue(
            ids.size() == ret.size() &&
            ret.containsAll(ids) &&
            ids.containsAll(ret)
        );
    }
}
