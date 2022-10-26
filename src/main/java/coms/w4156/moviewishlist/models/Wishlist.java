package coms.w4156.moviewishlist.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "wishlists",
    uniqueConstraints = @UniqueConstraint(columnNames = { "name", "user_id" })
)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class Wishlist implements ModelInterface<Long> {

    /**
     * ID of the wishlist.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Nullable
    @Getter
    @Setter
    private Long id;

    /**
     * Name of the wishlist.
     */
    @Getter
    @Setter
    private String name;

    /**
     * The user that owns this wishlist.
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    /**
     * The movies within this wishlist.
     */
    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "wishlist_movies",
            joinColumns = @JoinColumn(name = "wishlist_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    @Setter
    private List<Movie> movies;

    /**
     * Constructor to create a new Wishlist Object.
     *
     * @param name - Name of the wishlist
     * @param user - The user that this wishlist belongs to
     */
    public Wishlist(
        @JsonProperty final String name,
        @JsonProperty final User user
    ) {
        this.name = name;
        this.user = user;
    }

    /**
     * Add a movie to this wishlist.
     *
     * @param id - ID of the wishlist
     * @param name - Name of the wishlist
     * @param user - The user that this wishlist belongs to
     * @param movies - The movies within this wishlist
     */
    public Wishlist(
        @JsonProperty final Long id,
        @JsonProperty final String name,
        @JsonProperty final User user,
        @JsonProperty final List<Movie> movies
    ) {
        this.id = id;
        this.name = name;
        this.user = user;
        this.movies = movies;
    }

    /**
     * Get the email of the user that owns this wishlist.
     *
     * @return the email string
     */
    public String getUserId() {
        return this.user.getEmail();
    }

    /**
     * The get list of IDs of the movies that are stored within this wishlist.
     *
     * @return A list of Long Ids
     */
    public List<Long> getMovieIds() {
        return this.movies.stream()
            .map(movie -> movie.getId())
            .toList();
    }
}


