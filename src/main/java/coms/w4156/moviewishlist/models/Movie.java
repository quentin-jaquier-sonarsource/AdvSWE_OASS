package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
