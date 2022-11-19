package coms.w4156.moviewishlist.models.watchMode;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;
import lombok.Data;

@Data
public class TitleDetail {

    private Long id;
    private String title;

    @JsonAlias("original_title")
    private String originalTitle;

    @JsonAlias("plot_overview")
    private String plotOverview;

    private String type;

    @JsonAlias("runtime_minutes")
    private Integer runtimeMinutes;

    private Integer year;

    @JsonAlias("end_year")
    private Integer endYear;

    @JsonAlias("release_date")
    private String releaseDate;

    @JsonAlias("imdb_id")
    private String imdbId;

    @JsonAlias("tmdb_id")
    private Long tmdbId;

    @JsonAlias("tmdb_type")
    private String tmdbType;

    private List<Long> genres;

    @JsonAlias("genre_names")
    private List<String> genreNames;

    @JsonAlias("user_rating")
    private double userRating;

    @JsonAlias("critic_score")
    private Integer criticScore;

    @JsonAlias("us_rating")
    private String usRating;

    private String poster;
    private String backdrop;

    @JsonAlias("original_language")
    private String originalLanguage;

    @JsonAlias("similar_titles")
    private List<Long> similarTitlesIds;

    private List<Long> networks;

    @JsonAlias("network_names")
    private List<String> networkNames;

    private String trailer;

    @JsonAlias("trailer_thumbnail")
    private String trailerThumbnail;

    @JsonAlias("relevance_percentile")
    private double relevancePercentile;

    private List<WatchModeSource> sources;
}
