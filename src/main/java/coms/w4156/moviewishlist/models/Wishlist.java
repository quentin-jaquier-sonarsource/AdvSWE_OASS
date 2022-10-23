package coms.w4156.moviewishlist.models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(
    name = "wishlists",
    uniqueConstraints = @UniqueConstraint(columnNames = { "name", "user_id" })
)
@ToString
@EqualsAndHashCode
public class Wishlist implements ModelInterface<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Nullable
    @Getter
    private Long id;

    @Getter
    @Setter
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "wishlist_movies",
            joinColumns = @JoinColumn(name = "wishlist_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    @Setter
    private List<Movie> movies;

    protected Wishlist() {}

    public Wishlist(@JsonProperty String name, @JsonProperty User user) {
        this.name = name;
        this.user = user;
    }

    public Long getUserId() {
        return this.user.getId();
    }

    public List<Long> getMovieIds() {
        return this.movies.stream()
            .map(movie -> movie.getId())
            .toList();
    }
}


