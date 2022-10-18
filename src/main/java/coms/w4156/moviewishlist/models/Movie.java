package coms.w4156.moviewishlist.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "movies")
@ToString
public class Movie implements ModelInterface<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Getter
    private Long id;

    @Getter
    private String title;

    @Getter
    @Setter
    private Integer releaseYear;

    protected Movie() {}

    public Movie(@JsonProperty String title, @JsonProperty Integer releaseYear) {
        this.title = title;
        this.releaseYear = releaseYear;
    }

}
