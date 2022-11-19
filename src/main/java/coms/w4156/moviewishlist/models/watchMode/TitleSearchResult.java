package coms.w4156.moviewishlist.models.watchMode;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class TitleSearchResult {

    private Long id;
    private String name;
    private String type;
    private double relevance;
    private int year;

    @JsonAlias("tmdb_id")
    private Long tmdbId;

    @JsonAlias("tmdb_type")
    private String tmdbType;

    @JsonAlias("image_url")
    private String imageUrl;
}
