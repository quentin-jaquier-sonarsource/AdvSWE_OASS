package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.models.Client;
import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.repositories.ProfileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
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
        Client c = Client.builder().id(Long.parseLong("1"))
                .email("x@test.com").build();
        Profile p11 = Profile.builder().client(c).id(Long.parseLong("2")).build();
        Profile p12 = Profile.builder().client(c).id(Long.parseLong("4")).build();


        Client c2 = Client.builder().id(Long.parseLong("2"))
                .email("x@test.com").build();
        Profile p2 = Profile.builder().client(c2).id(Long.parseLong("1")).build();

        List<Profile> profileList = new ArrayList<>();

        profileList.add(p11);
        profileList.add(p12);

        Mockito
                .when(profileRepository.findAll())
                .thenReturn(profileList.stream()
                        .filter(p -> p.getClientId() == c.getId()).toList());

        List<Profile> ret = profileService.getAll().stream().
                filter(p -> p.getClientId() == c.getId()).toList();
        Assertions.assertNotNull(ret);
        Assertions.assertEquals(ret.size(), profileList.size());

    }

    @Test
    void getAllForClientFailTest() {
        Client c = Client.builder().id(Long.parseLong("1"))
                .email("x@test.com").build();
        Profile p11 = Profile.builder().client(c).id(Long.parseLong("2")).build();
        Profile p12 = Profile.builder().client(c).id(Long.parseLong("4")).build();


        Client c2 = Client.builder().id(Long.parseLong("2"))
                .email("x@test.com").build();
        Profile p2 = Profile.builder().client(c2).id(Long.parseLong("1")).build();

        List<Profile> profileList = new ArrayList<>();

        profileList.add(p11);
        profileList.add(p12);
        profileList.add(p2);

        Mockito
                .when(profileRepository.findAll())
                .thenReturn(profileList.stream()
                        .filter(p -> p.getClientId() == c.getId()).toList());

        List<Profile> ret = profileService.getAll().stream().
                filter(p -> p.getClientId() == c.getId()).toList();
        Assertions.assertNotNull(ret);
        Assertions.assertFalse(ret.size() == profileList.size());
    }

    @Test
    void findByIdTest() {
        Client c = Client.builder().id(Long.parseLong("1"))
                .email("x@test.com").build();
        Profile p11 = Profile.builder().client(c).id(Long.parseLong("2")).build();
        Profile p12 = Profile.builder().client(c).id(Long.parseLong("4")).build();

        Mockito
                .when(profileRepository.findById(p12.getId()))
                .thenReturn(Optional.of(p12));

        Optional<Profile> ret = profileService.findById(p12.getId());
        Assertions.assertNotNull(ret);
        Assertions.assertTrue(ret.get().getId() == p12.getId());
    }

    @Test
    void findByIdFailTest() {
        Client c = Client.builder().id(Long.parseLong("1"))
                .email("x@test.com").build();
        Profile p11 = Profile.builder().client(c).id(Long.parseLong("2")).build();
        Profile p12 = Profile.builder().client(c).id(Long.parseLong("4")).build();

        Mockito
                .when(profileRepository.findById(p12.getId()))
                .thenReturn(Optional.of(p12));

        Optional<Profile> ret = profileService.findById(p12.getId());
        Assertions.assertNotNull(ret);
        Assertions.assertFalse(ret.get().getId() == p11.getId());
    }
}