package coms.w4156.moviewishlist.modelTests;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Profile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class ClientTests {
    @Test
    void testToString() {
        Client client = new Client();

        Assertions.assertEquals("Client(id=null, email=null, profiles=[], roles=null)", client.toString());
    }

    @Test
    void testEquals() {
        Client client = new Client();
        Client client1 = new Client();

        Assertions.assertEquals(client1, client);
    }

    @Test
    void testSetProfilesAndGetProfNotEmpty() {
        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        List<Profile> profiles = List.of(profile1, profile2);
        Client client = new Client();

        client.setProfiles(profiles);

        Assertions.assertEquals(profiles, client.getProfiles());
    }

    @Test
    void testGetProfilesEmpty() {

        List<Profile> profiles = new ArrayList<>();
        Client client = new Client();

        client.setProfiles(profiles);

        Assertions.assertEquals(List.of(), client.getProfiles());
    }
}
