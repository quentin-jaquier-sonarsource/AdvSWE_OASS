package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
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
    @JoinColumn(name = "user_id")
    @Setter
    private User user;


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


    public Ratings(
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

//    @Override
//    public String toString() {
//        return "";
//    }
}


