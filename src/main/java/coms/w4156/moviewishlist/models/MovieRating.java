package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(
        name = "movie_rating"
)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class MovieRating implements ModelInterface<Long> {



    @Id
    @Setter
    private Long id;

    @Getter
    @Setter
    private String review;

    @Getter
    @Setter
    private Double rating;


    public MovieRating(
            @JsonProperty final Long id,
            @JsonProperty final String review,
            @JsonProperty final Double rating
    ) {
        this.id = id;
        this.rating = rating;
        this.review = review;
    }

    @Override
    public Long getId() {
        return null;
    }

//    public Long getMovieId() {
//        return this.id;
//    }
//



}
