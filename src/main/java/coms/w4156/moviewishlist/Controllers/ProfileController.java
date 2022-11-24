package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.models.Profile;
import coms.w4156.moviewishlist.services.ProfileService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/profiles")
@RestController
public class ProfileController {

    /**
     * Use dependency injection to inject an object of the ProfileService class.
     */
    @Autowired
    private ProfileService profileService;

    /**
     * Fetch all profiles in the database.
     *
     * @return List of Profile objects
     */
    @GetMapping
    public ResponseEntity<List<Profile>> getAll() {
        return new ResponseEntity<>(profileService.getAll(), HttpStatus.OK);
    }

    /**
     * Get a particular profile by ID. If not found HTTP 204: NO Content response.
     *
     * @param id - The email address of the profile
     * @return A single Profile object
     */
    @GetMapping("/{id}")
    public ResponseEntity<Profile> getProfileById(@PathVariable long id) {
        return profileService.findById(id)
            .map(profile -> new ResponseEntity<>(profile, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * POST `/profiles` will create a new profile. The fields for the profile object
     * must be passed in as the RequestBody as json.path.
     *
     * @param profile - Profile object to add to the database.
     * @return The profile object that was just created
     */
    @PostMapping
    public ResponseEntity<Profile> createProfile(@RequestBody final Profile profile) {
        if  (profile.getName().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(profileService.create(profile), HttpStatus.OK);
    }

    /**
     * PUT `/profiles/{id}` will update an existing profiles with the given ID.
     * The updated fields for the profile should be passed in as the JSON
     * Request Body.
     *
     * @param id      - email of the profile to update
     * @param newData - profile data for the updated profile.
     * @return The newly updated profile
     */
    @PutMapping("/{id}")
    public ResponseEntity<Profile> updateProfile(
        @PathVariable final long id,
        @RequestBody final Profile newData
    ) {
        return profileService
            .findById(id)
            .map(profile -> {
                profile.setName(newData.getName());
                profileService.update(profile);
                return new ResponseEntity<>(profile, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    /**
     * Delete all profiles.
     *
     * @return The list of profiles that were just deleted
     */
    @DeleteMapping
    public ResponseEntity<List<Profile>> deleteAllProfiles() {
        profileService.deleteAll();
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
    }

    /**
     * Delete a particular profile by ID.
     *
     * @param id The id of the profile to delete
     * @return the profile that was just deleted
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Profile> deleteWishlist(@PathVariable long id) {
        return profileService.deleteById(id)
            .map(profile -> new ResponseEntity<>(profile, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }
}
