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
public class Movie implements ModelInterface<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Getter
    private Long id;

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private Integer releaseYear;

    @ManyToMany(mappedBy = "movies")
    @Setter
    private List<Wishlist> wishlists = new ArrayList<>();

    /**
     * Create a new Movie object
     * @param title - Title of the movie
     * @param releaseYear - The year when the movie came out
     */
    public Movie(@JsonProperty String title, @JsonProperty Integer releaseYear) {
        this.title = title;
        this.releaseYear = releaseYear;
    }

    /**
     * Fetch the IDs of the wishlists associated with this movie
     * @return list of Long IDs
     */
    public List<Long> getWishlistIds() {
        return this.wishlists.stream()
            .map(wishlist -> wishlist.getId())
            .toList();
    }

}
