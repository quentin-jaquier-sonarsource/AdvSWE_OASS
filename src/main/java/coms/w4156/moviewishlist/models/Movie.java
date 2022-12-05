package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "movies")
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class Movie implements ModelInterface<Long> {

    /**
     * ID of the movie on WatchMode.
     */
    @Id
    @Getter
    @Setter
    @Column(
        name = "watchmode_id",
        nullable = false,
        unique = true,
        updatable = false
    )
    private Long id;

    /**
     * The wishlists that contain this movie.
     */
    @ManyToMany(mappedBy = "movies")
    @Setter
    @Builder.Default
    private List<Wishlist> wishlists = new ArrayList<>();

    @Getter
    @Setter
    @Column(
        nullable = true,
        unique = false,
        updatable = false
    )
    private String name;

    @Getter
    @Setter
    @Column(
        nullable = true,
        unique = false,
        updatable = true
    )
    private String genre;

    @Getter
    @Setter
    @Column(
        nullable = true,
        unique = false,
        updatable = false
    )
    private String releaseYear;

    @Getter
    @Setter
    @Column(
        nullable = true,
        unique = false,
        updatable = false
    )
    private int runtime;

    @Getter
    @Setter
    @Column(
        nullable = true,
        unique = false,
        updatable = false
    )
    private int criticScore;

    /**
     * The ratings given for this movie.
     */
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @Getter
    private List<Rating> ratings;

    /**
     * Create a new Movie object.
     *
     * @param id - ID of the movie on WatchMode
     * @param wishlists - The wishlists that contain this movie
     * @param movie_name - Name of the movie
     * @param movie_gener - Movie genre
     * @param movie_release_year - Release year of the movie
     * @param movie_runtime - runtime of the movie
     * @param critic_score - critic score of the movie
     */
    public Movie(
        @JsonProperty final Long id,
        @JsonProperty final List<Wishlist> wishlists,
        @JsonProperty final String name,
        @JsonProperty final String genre,
        @JsonProperty final String releaseYear,
        @JsonProperty final int runtime,
        @JsonProperty final int criticScore
    ) {
        this.id = id;
        this.wishlists = wishlists;
        this.name = name;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.runtime = runtime;
        this.criticScore = criticScore;
        if (this.wishlists == null) {
            this.wishlists = List.of();
        }
    }

    /**
     * Get wishlists that this movie belongs to.
     *
     * @return List of wishlists
     */
    public List<Wishlist> getWishlists() {
        if (this.wishlists == null) {
            return List.of();
        }
        return wishlists;
    }
}
