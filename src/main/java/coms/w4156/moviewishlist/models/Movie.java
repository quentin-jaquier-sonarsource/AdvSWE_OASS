package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
    private String title;

    @Column(nullable = true, unique = false, updatable = true)
    private String genreString;

    @Getter
    @Setter
    @Column(
        nullable = true,
        unique = false,
        updatable = false
    )
    private Integer releaseYear;

    @Getter
    @Setter
    @Column(
        nullable = true,
        unique = false,
        updatable = false
    )
    private Integer runtimeMinutes;

    @Getter
    @Setter
    @Column(
        nullable = true,
        unique = false,
        updatable = false
    )
    private Integer criticScore;

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
     * @param title - Name of the movie
     * @param genres - Movie genres
     * @param releaseYear - Release year of the movie
     * @param runtimeMinutes - runtime of the movie
     * @param criticScore - critic score of the movie
     */
    public Movie(
        @JsonProperty final Long id,
        @JsonProperty final List<Wishlist> wishlists,
        @JsonProperty final String title,
        @JsonProperty final List<String> genres,
        @JsonProperty final Integer releaseYear,
        @JsonProperty final Integer runtimeMinutes,
        @JsonProperty final Integer criticScore
    ) {
        this.id = id;
        this.wishlists = wishlists;
        this.title = title;
        this.setGenres(genres);
        this.releaseYear = releaseYear;
        this.runtimeMinutes = runtimeMinutes;
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

    /**
     * Get the genres of the movie as a list.
     * @return The genres of the movie as a list.
     */
    public List<String> getGenres() {
        return Arrays
            .stream(this.genreString.split(","))
            .collect(Collectors.toList());
    }

    /**
     * Set the genres of the movie as a list.
     * @param genres The genres of the movie as a list.
     */
    public void setGenres(final List<String> genres) {
        this.genreString = String.join(",", genres);
    }
}
