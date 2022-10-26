package coms.w4156.moviewishlist.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class User implements ModelInterface<String> {
    /**
     * Email of the user.
     */
    @Id
    @Getter
    @Setter
    private String email;

    /**
     * Name of the user.
     */
    @Getter
    @Setter
    private String name;

    /**
     * Hashed Password of the user.
     */
    @ToString.Exclude
    @Getter
    private String password;

    /**
     * The list of wishlists owned by this user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wishlist> wishlists;

    /**
     * Constructor for the User class.
     * @param email - Email of the user to be created
     * @param name - Name of the user
     * @param password - Password of the user
     */
    public User(
        @JsonProperty final String email,
        @JsonProperty final String name,
        @JsonProperty final String password
    ) {
        this.email = email;
        this.name = name;
        this.password = hashPassword(password);
    }

    /**
     * Constructor for the User class.
     * @param email - Email of the user to be created
     * @param name - Name of the user
     * @param password - Password of the user
     * @param wishlists - List of wishlists owned by the user
     */
    public User(
        @JsonProperty final String email,
        @JsonProperty final String name,
        @JsonProperty final String password,
        @JsonProperty final List<Wishlist> wishlists
    ) {
        this.email = email;
        this.name = name;
        this.password = hashPassword(password);
        this.wishlists = wishlists;
    }

    /**
     * Compare a given password with user's password and check if they are the
     * same.
     *
     * This is important as the passwords should not be saved as plain text.
     * So the newly given password will be hashed and compared against the
     * hashed password saved in this User object.
     *
     * @param comparePassword - The password to compare
     * @return Did the two password match?
     */
    public Boolean checkPassword(final String comparePassword) {
        return hashPassword(comparePassword)
            .equals(hashPassword(this.password));
    }

    /**
     * Get the ID of the user whish is the Email of the user.
     *
     * @return the email of the user
     */
    @Override
    public String getId() {
        return this.getEmail();
    }

    /**
     * Get the IDs of the list of wishlists that are owned by this user.
     *
     * @return a List of Long ID of wishlists
     */
    public List<Long> getWishlistIds() {
        return this.wishlists.stream()
            .map(wishlist -> wishlist.getId())
            .toList();
    }

    /**
     * Update the password of this user.
     *
     * @param newPassword
     * @return The updated user object
     */
    public User setPassword(final String newPassword) {
        this.password = hashPassword(newPassword);
        return this;
    }

    /**
     * Take a plain-text password, hash it and retun the hash.
     *
     * @param plainPassword - The plain-text password
     * @return - the hash of the given password
     */
    private String hashPassword(final String plainPassword) {
        // We can use a hashing function here later
        // Or we can find a better way to do auth
        return plainPassword;
    }

}
