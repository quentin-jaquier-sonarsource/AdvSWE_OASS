package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
public class User implements ModelInterface<Long> {
    /**
     * Email of the user.
     */
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    // TODO: resolve conflicts where email was Id before

    @Getter
    @Setter
    private String email;

    /**
     * Name of the user.
     */
    @Getter
    @Setter
    private String username;

    /**
     * Hashed Password of the user.
     */
    @ToString.Exclude
    @Getter
    @Setter
    private String encodedPassword;

    /**
     * The list of wishlists owned by this user.
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Getter
    private List<Wishlist> wishlists;
<<<<<<< HEAD

    public User(@JsonProperty String email, @JsonProperty String username, @JsonProperty String encodedPassword, @JsonProperty final List<Wishlist> wishlists) {
        this.email = email;
        this.username = username;
        this.encodedPassword = encodedPassword;
=======

    /**
     * Constructor for the User class.
     *
     * @param email    - Email of the user to be created
     * @param name     - Name of the user
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
     *
     * @param email     - Email of the user to be created
     * @param name      - Name of the user
     * @param password  - Password of the user
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
>>>>>>> main
        this.wishlists = wishlists;
    }

    public User(Long id, @JsonProperty String email, @JsonProperty String username, @JsonProperty String encodedPassword, @JsonProperty final List<Wishlist> wishlists) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.encodedPassword = encodedPassword;
        this.wishlists = wishlists;
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
<<<<<<< HEAD
=======

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
>>>>>>> main
}
