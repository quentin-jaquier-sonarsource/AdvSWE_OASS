package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;


@Entity
@Table(name = "movies")
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@AllArgsConstructor
public class Movie implements ModelInterface<Long> {

    /**
     * ID of the movie.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Getter
    @Setter
    private Long id;

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

    @Getter
    @Setter
    @Column(
            name = "movie_name",
            nullable = false,
            unique = true,
            updatable = false
    )
    private String movieName;

    @Getter
    @Setter
    @Column(
            name = "movie_genre",
            nullable = false,
            unique = false,
            updatable = true
    )
    private String genre;

    @Getter
    @Setter
    @Column(
            name = "movie_release_year",
            nullable = false,
            unique = false,
            updatable = true
    )
    private int movieReleaseYear;


    /**
     * The wishlists that contain this movie.
     */
    @ManyToMany(mappedBy = "movies")
    @Setter
    @Builder.Default
    @Getter
    private List<Wishlist> wishlists = new ArrayList<>();


    /**
     * The ratings given for this movie.
     */
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @Getter
    private List<Ratings> ratings;

    @Getter
    @Setter
    @Column(
            name = "movie_name",
            nullable = false,
            unique = true,
            updatable = false
    )
    private String movie_name;

    @Getter
    @Setter
    @Column(
            name = "movie_gener",
            nullable = false,
            unique = false,
            updatable = true
    )
    private String movie_gener;

    @Getter
    @Setter
    @Column(
            name = "movie_release_year",
            nullable = false,
            unique = false,
            updatable = false
    )
    private String movie_release_year;

    @Getter
    @Setter
    @Column(
            name = "movie_runtime",
            nullable = false,
            unique = false,
            updatable = false
    )
    private int movie_runtime;

    @Getter
    @Setter
    @Column(
            name = "critic_score",
            nullable = false,
            unique = false,
            updatable = false
    )
    private int critic_score;

    /**
     * Create a new Movie object.
     *
     * @param id - ID of the movie
     * @param movieName - Name of the movie
     * @param genre - Movie genre
     * @param movieReleaseYear - Release year of the movie
     * @param wishlists - The wishlists that contain this movie
     */
    public Movie(
        @JsonProperty final Long id,
        @JsonProperty final String movie_name,
        @JsonProperty final String movie_gener,
        @JsonProperty final String movie_release_year,
        @JsonProperty final int movie_runtime,
        @JsonProperty final int critic_score,
        @JsonProperty final List<Wishlist> wishlists
    ) {
        this.id = id;
        this.movie_name = movie_name;
        this.movie_gener = movie_gener;
        this.movie_release_year = movie_release_year;
        this.movie_runtime = movie_runtime;
        this.critic_score = critic_score;
        this.wishlists = wishlists;
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
