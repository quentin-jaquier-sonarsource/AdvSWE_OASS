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
        name = "movie_name",
        nullable = true,
        unique = false,
        updatable = false
    )
    private String movie_name;

    @Getter
    @Setter
    @Column(
        name = "movie_gener",
        nullable = true,
        unique = false,
        updatable = true
    )
    private String movie_gener;

    @Getter
    @Setter
    @Column(
        name = "movie_release_year",
        nullable = true,
        unique = false,
        updatable = false
    )
    private String movie_release_year;

    @Getter
    @Setter
    @Column(
        name = "movie_runtime",
        nullable = true,
        unique = false,
        updatable = false
    )
    private int movie_runtime;

    @Getter
    @Setter
    @Column(
        name = "critic_score",
        nullable = true,
        unique = false,
        updatable = false
    )
    private int critic_score;

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
        @JsonProperty final String movie_name,
        @JsonProperty final String movie_gener,
        @JsonProperty final String movie_release_year,
        @JsonProperty final int movie_runtime,
        @JsonProperty final int critic_score
    ) {
        this.id = id;
        this.wishlists = wishlists;
        this.movie_name = movie_name;
        this.movie_gener = movie_gener;
        this.movie_release_year = movie_release_year;
        this.movie_runtime = movie_runtime;
        this.critic_score = critic_score;
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
