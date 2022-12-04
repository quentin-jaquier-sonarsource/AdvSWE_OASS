package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;



@Entity
@Table(
        name = "ratings"
)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class Rating implements ModelInterface<Long> {


    /**
     * ID of the user rating.
     */
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    /**
     * The profile that gave this rating.
     */
    @ManyToOne
    @JoinColumn(name = "profile_id")
    @Setter
    private Profile profile;

    /**
     * The movie for which the rating is given.
     */
    @ManyToOne
    @JoinColumn(name = "movie_id")
    @Setter
    private Movie movie;

    /**
     * The review(comment) left with the rating.
     */
    @Getter
    @Setter
    private String review;

    /**
     * The numerical rating given to a movie.
     */
    @Getter
    @Setter
    private Double rating;



    /**
     * Constructor to create a new Wishlist Object.
     *
     * @param review - Comment left with the rating
     * @param rating - The numerical rating given to a movie
     */
    public Rating(
            @JsonProperty final String review,
            @JsonProperty final Double rating
    ) {
        this.review = review;
        this.rating = rating;
    }

    /**
     * Constructor to create a new Wishlist Object.
     *
     * @param profile - The profile that gave this rating
     * @param movie - The movie for which the rating is given.
     * @param review - Comment left with the rating
     * @param rating - The numerical rating given to a movie
     */
    public Rating(
            @JsonProperty final Profile profile,
            @JsonProperty final Movie movie,
            @JsonProperty final String review,
            @JsonProperty final Double rating
    ) {
        this.profile = profile;
        this.movie = movie;
        this.rating = rating;
        this.review = review;
    }

    /**
     * Constructor to create a new Wishlist Object.
     *
     * @param id - Id of the rating
     * @param profile - The profile that gave this rating
     * @param movie - The movie for which the rating is given.
     * @param review - Comment left with the rating
     * @param rating - The numerical rating given to a movie
     */
    public Rating(
            @JsonProperty final Long id,
            @JsonProperty final Profile profile,
            @JsonProperty final Movie movie,
            @JsonProperty final String review,
            @JsonProperty final Double rating
    ) {
        this.id = id;
        this.profile = profile;
        this.movie = movie;
        this.rating = rating;
        this.review = review;
    }




    /**
     * Get the Id of the profile that that gave this rating.
     *
     * @return the Id of the profile
     */
    public Long getProfileId() {
        return this.profile.getId();
    }

    /**
     * Get the Id of the client responsible for this rating.
     *
     * @return the Id of the client
     */
    public Long getClientId() {
        return this.profile.getClient().getId();
    }

    /**
     * Get the ID of the movie for which the rating is given.
     *
     * @return A Long Id
     */
    public Long getMovieId() {
        return this.movie.getId();
    }

}


