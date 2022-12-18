package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.repositories.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProfileServiceTest {

    @InjectMocks
    ProfileService profileService;

    @Mock
    ProfileRepository profileRepository;

    @Test
    void findAllTest() {
        Client c = Client.builder().id(Long.parseLong("1"))
                .email("x@test.com").build();

        Client c2 = Client.builder().id(Long.parseLong("2"))
                .email("x@test.com").build();

        List<Profile> profileList = new ArrayList<>();

        profileList.add(Profile.builder().client(c).id(Long.parseLong("2")).build());
        profileList.add(Profile.builder().client(c2).id(Long.parseLong("1")).build());

        Mockito
                .when(profileRepository.findAll())
                .thenReturn(profileList);

        List<Profile> ret = profileService.getAll();
        Assertions.assertNotNull(ret);
        Assertions.assertEquals(ret.size(), profileList.size());
    }

    @Test
    void findAllFailTest() {
        Client c = Client.builder().id(Long.parseLong("1"))
                .email("x@test.com").build();

        Client c2 = Client.builder().id(Long.parseLong("2"))
                .email("x@test.com").build();

        List<Profile> profileList = new ArrayList<>();

        profileList.add(Profile.builder().client(c).id(Long.parseLong("2")).build());
        //profileList.add(Profile.builder().client(c2).id(Long.parseLong("1")).build());

        Mockito
                .when(profileRepository.findAll())
                .thenReturn(profileList);

        List<Profile> ret = profileService.getAll();
        Assertions.assertNotNull(ret);
        Assertions.assertFalse(ret.size() != profileList.size());
    }

    @Test
    void getAllForClientTest() {
    }

    @Test
    void getAllForClientFailTest() {
    }

    @Test
    void findByIdTest() {
    }

    @Test
    void findByIdFailTest() {
    }
}