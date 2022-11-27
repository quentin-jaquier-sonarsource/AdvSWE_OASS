package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

import lombok.*;

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
    @Getter
    @Setter
    @Column(
        name = "watchmode_id",
        nullable = false,
        unique = true,
        updatable = false
    )
    private Long watchModeId;

    /**
     * The wishlists that contain this movie.
     */
    @ManyToMany(mappedBy = "movies")
    @Setter
    @Builder.Default
    private List<Wishlist> wishlists = new ArrayList<>();



    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @Getter
    private List<Ratings> ratings;

    /**
     * Create a new Movie object.
     *
     * @param id - ID of the movie
     * @param watchModeId - ID of the movie on WatchMode
     * @param wishlists - The wishlists that contain this movie
     */
    public Movie(
        @JsonProperty final Long id,
        @JsonProperty final Long watchModeId,
        @JsonProperty final List<Wishlist> wishlists
    ) {
        this.id = id;
        this.watchModeId = watchModeId;
        this.wishlists = wishlists;
    }
}
