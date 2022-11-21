package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;


@Entity
@Table(
        name = "user_ratings"
)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class UserRating implements ModelInterface<Long> {


    /**
     * ID of the user rating.
     */
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Getter
    @Setter
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @Getter
    @Setter
    @ManyToOne(cascade=CascadeType.ALL)
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Getter
    @Setter
    private String review;

    @Getter
    @Setter
    private Double rating;



    public UserRating(
            @JsonProperty final User users,
            @JsonProperty final Movie movies,
            @JsonProperty final String review,
            @JsonProperty final Double rating
    ) {
        this.user = users;
        this.movie = movies;
        this.rating = rating;
        this.review = review;
    }


    public UserRating(
            @JsonProperty final Long id,
            @JsonProperty final User users,
            @JsonProperty final Movie movies,
            @JsonProperty final String review,
            @JsonProperty final Double rating
    ) {
        this.id = id;
        this.user = users;
        this.movie = movies;
        this.rating = rating;
        this.review = review;
    }




    public String getUserId() {
        return this.user.getEmail();
    }

    public Long getMovieIds() {
        return this.movie.getId();
    }
}
