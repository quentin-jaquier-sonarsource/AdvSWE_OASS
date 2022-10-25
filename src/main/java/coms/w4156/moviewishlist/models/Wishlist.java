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
public class Wishlist implements ModelInterface<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Nullable
    @Getter
    private Long id;

    @Getter
    @Setter
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "wishlist_movies",
            joinColumns = @JoinColumn(name = "wishlist_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    @Setter
    private List<Movie> movies;

    /**
     * Constructor to create a new Wishlist Object
     * @param name - Name of the wishlist
     * @param user - The user that this wishlist belongs to
     */
    public Wishlist(@JsonProperty String name, @JsonProperty User user) {
        this.name = name;
        this.user = user;
    }

    /**
     * Get the email of the user that owns this wishlist
     * @return the email string
     */
    public String getUserId() {
        return this.user.getEmail();
    }

    /**
     * The get list of IDs of the movies that are stored within this wishlist
     * @return A list of Long Ids
     */
    public List<Long> getMovieIds() {
        return this.movies.stream()
            .map(movie -> movie.getId())
            .toList();
    }
}


