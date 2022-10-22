package coms.w4156.moviewishlist.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.*;
import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "movies")
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@Builder
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

    public Movie(@JsonProperty String title, @JsonProperty Integer releaseYear) {
        this.title = title;
        this.releaseYear = releaseYear;
    }

    public List<Long> getWishlistIds() {
        return this.wishlists.stream()
            .map(wishlist -> wishlist.getId())
            .toList();
    }

}
