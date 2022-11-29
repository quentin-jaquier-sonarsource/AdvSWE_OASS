package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
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

//    @Getter
//    @Setter
//    @Column(
//            name = "movie_director",
//            nullable = false,
//            unique = true,
//            updatable = false
//    )
//    private List<Wishlist> wishlists = new ArrayList<>();



    /**
     * Create a new Movie object.
     *
     * @param id - ID of the movie on WatchMode
     * @param wishlists - The wishlists that contain this movie
     */
    public Movie(
        @JsonProperty final Long id,
        @JsonProperty final String movie_name,
        @JsonProperty final String movie_gener,
        @JsonProperty final String movie_release_year,
        @JsonProperty final int movie_runtime,
        @JsonProperty final List<Wishlist> wishlists
    ) {
        this.id = id;
        this.movie_name = movie_name;
        this.movie_gener = movie_gener;
        this.movie_release_year = movie_release_year;
        this.movie_runtime = movie_runtime;
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
