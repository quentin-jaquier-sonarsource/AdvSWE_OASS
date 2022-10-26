package coms.w4156.moviewishlist.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

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
     * ID of the movie.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Getter
    @Setter
    private Long id;

    /**
     * Title of the movie.
     */
    @Getter
    @Setter
    private String title;

    /**
     * The year when the movie released.
     */
    @Getter
    @Setter
    private Integer releaseYear;

    /**
     * The wishlists that contain this movie.
     */
    @ManyToMany(mappedBy = "movies")
    @Setter
    @Builder.Default
    private List<Wishlist> wishlists = new ArrayList<>();

    /**
     * Create a new Movie object.
     * @param title - Title of the movie
     * @param releaseYear - The year when the movie came out
     */
    public Movie(
        @JsonProperty final String title,
        @JsonProperty final Integer releaseYear
    ) {
        this.title = title;
        this.releaseYear = releaseYear;
    }

    /**
     * Create a new Movie object.
     *
     * @param id - ID of the movie
     * @param title - Title of the movie
     * @param releaseYear - The year when the movie came out
     * @param wishlists - The wishlists that contain this movie
     */
    public Movie(
        @JsonProperty final Long id,
        @JsonProperty final String title,
        @JsonProperty final Integer releaseYear,
        @JsonProperty final List<Wishlist> wishlists
    ) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.wishlists = wishlists;
    }

    /**
     * Fetch the IDs of the wishlists associated with this movie.
     * @return list of Long IDs
     */
    public List<Long> getWishlistIds() {
        return this.wishlists.stream()
            .map(wishlist -> wishlist.getId())
            .toList();
    }

}
