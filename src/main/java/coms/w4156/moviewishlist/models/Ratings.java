package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;

import javax.persistence.*;


@Entity
@Table(
        name = "ratings"
)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class Ratings implements ModelInterface<Long> {


    /**
     * ID of the user rating.
     */
    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "profile_id")
    @Setter
    private Profile profile;


    @ManyToOne
    @JoinColumn(name = "movie_id")
    @Setter
    private Movie movie;

    @Getter
    @Setter
    private String review;

    @Getter
    @Setter
    private Double rating;



    public Ratings(
            @JsonProperty final String review,
            @JsonProperty final Double rating
    ) {
        this.review = review;
        this.rating = rating;
    }

    public Ratings(
            @JsonProperty final Profile profile,
            @JsonProperty final Movie movies,
            @JsonProperty final String review,
            @JsonProperty final Double rating
    ) {
        this.profile = profile;
        this.movie = movies;
        this.rating = rating;
        this.review = review;
    }


    public Ratings(
            @JsonProperty final Long id,
            @JsonProperty final Profile profile,
            @JsonProperty final Movie movies,
            @JsonProperty final String review,
            @JsonProperty final Double rating
    ) {
        this.id = id;
        this.profile = profile;
        this.movie = movies;
        this.rating = rating;
        this.review = review;
    }




    public Long getProfileId() {
        return this.profile.getId();
    }

    public Long getMovieIds() {
        return this.movie.getId();
    }

}


