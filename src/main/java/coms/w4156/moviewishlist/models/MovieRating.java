package coms.w4156.moviewishlist.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;


@Entity
@Table(
        name = "movie_rating"
)
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Builder
public class MovieRating implements ModelInterface<Movie> {



    @Id
    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @Getter
    @Setter
    private String review;

    @Getter
    @Setter
    private Double rating;


    public MovieRating(
            @JsonProperty final Movie movie,
            @JsonProperty final String review,
            @JsonProperty final Double rating
    ) {
        this.movie = movie;
        this.rating = rating;
        this.review = review;
    }

    public Long getMovieId() {
        return this.movie.getId();
    }

    @Override
    public Movie getId() {
        return null;
    }


}
