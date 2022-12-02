package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "profiles")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Profile implements ModelInterface<Long> {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * Name of the profile.
     */
    @Getter
    @Setter
    private String name;

    /**
     * The list of wishlists owned by this profile.
     */
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    @Getter
    private List<Wishlist> wishlists;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @Getter
    @Setter
    private Client client;


    /**
     * The ratings given by this profile.
     */
    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL)
    @Getter
    private List<Rating> ratings;

    /**
     * Constructor for the Profile class.
     *
     * @param name     - Name of the profile
     */
    public Profile(@JsonProperty final String name) {
        this.name = name;
    }

    /**
     * Constructor for the Profile class.
     *
     * @param name      - Name of the profile
     * @param wishlists - List of wishlists owned by the profile
     * @param client    - The client that services this profile
     */
    public Profile(
        @JsonProperty final String name,
        @JsonProperty final List<Wishlist> wishlists,
        @JsonProperty final Client client
    ) {
        this.name = name;
        this.wishlists = wishlists;
        this.client = client;
    }

    /**
     * Create a new Profile object.
     * @param id
     * @param name
     * @param wishlists
     * @param client
     */
    public Profile(
        final Long id,
        @JsonProperty final String name,
        @JsonProperty final List<Wishlist> wishlists,
        final Client client
    ) {
        this.id = id;
        this.name = name;
        this.wishlists = wishlists;
        this.client = client;
    }

    /**
     * Get the IDs of the list of wishlists that are owned by this profile.
     *
     * @return a List of Long ID of wishlists
     */
    public List<Long> getWishlistIds() {
        return this.wishlists.stream()
            .map(wishlist -> wishlist.getId())
            .toList();
    }

    /**
     * Get the Id of the client responsible for this profile
     *
     * @return the Id of the client responsible for this profile
     */
    public Long getClientId() {
        return this.client.getId();
    }
}
