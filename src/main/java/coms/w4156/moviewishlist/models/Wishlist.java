package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Entity
@Table(
    name = "wishlists",
    uniqueConstraints = @UniqueConstraint(
        columnNames = { "name", "profile_id" }
    )
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
     * The profile that owns this wishlist.
     */
    @ManyToOne
    @JoinColumn(name = "profile_id")
    @Setter
    private Profile profile;

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
    @Getter
    private List<Movie> movies = new ArrayList<>();

    /**
     * Constructor to create a new Wishlist Object.
     *
     * @param name - Name of the wishlist
     * @param profile - The profile that this wishlist belongs to
     */
    public Wishlist(
        @JsonProperty final String name,
        @JsonProperty final Profile profile
    ) {
        this.name = name;
        this.profile = profile;
    }

    /**
     * Add a movie to this wishlist.
     *
     * @param id - ID of the wishlist
     * @param name - Name of the wishlist
     * @param profile - The profile that this wishlist belongs to
     * @param movies - The movies within this wishlist
     */
    public Wishlist(
        @JsonProperty final Long id,
        @JsonProperty final String name,
        @JsonProperty final Profile profile,
        @JsonProperty final List<Movie> movies
    ) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.movies = movies;
    }

    /**
     * Get the Id of the profile that owns this wishlist.
     *
     * @return the Id of the profile
     */
    public Long getProfileId() {
        return this.profile.getId();
    }

    /**
     * Get the Id of the cleint responsible for this wishlist.
     *
     * @return the Id of the client
     */
    public Long getClientId() {
        return this.profile.getClientId();
    }

    /**
     * The get list of IDs of the movies that are stored within this wishlist.
     *
     * @return A list of Long Ids
     */
    public List<Long> getMovieIds() {
        return this.movies.stream().map(movie -> movie.getId()).toList();
    }

    /**
     * Get movies by a given genre.
     *
     * @param genre - The genre to filter by
     * @return List of movies
     */
    public List<Movie> getMoviesByGenre(final String genre) {
        return this.movies.stream()
            .filter(movie -> movie.getGenres().contains(genre))
            .collect(Collectors.toList());
    }

    /**
     * Get movies by a release year.
     *
     * @param releaseYear - The release year of the movie
     * @return List of movies
     */
    public List<Movie> getMoviesByReleaseYear(final Integer releaseYear) {
        return this.movies.stream()
            .filter(movie -> movie.getReleaseYear().equals(releaseYear))
            .collect(Collectors.toList());
    }

    /**
     * Get movies by a given runtime.
     *
     * @param runtime - The runtime of the movie
     * @return List of movies
     */
    public List<Movie> getMoviesByRuntime(final Integer runtime) {
        return this.movies.stream()
            .filter(movie -> movie.getRuntimeMinutes() == runtime)
            .collect(Collectors.toList());
    }

    /**
     * Get movies by a given critics score.
     *
     * @param criticScore - The critic score of the movie
     * @return List of movies
     */
    public List<Movie> getMoviesByCriticScore(final Integer criticScore) {
        return this.movies.stream()
            .filter(movie -> movie.getCriticScore() == criticScore)
            .collect(Collectors.toList());
    }
}
