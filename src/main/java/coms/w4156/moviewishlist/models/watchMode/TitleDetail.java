package coms.w4156.moviewishlist.models.watchMode;

import java.util.List;

public record TitleDetail(
    Long id,
    String title,
    String original_title,
    String plot_overview,
    String type,
    Integer runtime_minutes,
    Integer year,
    Integer end_year,
    String release_date,
    String imdb_id,
    Long tmdb_id,
    String tmdb_type,
    List<Long> genres,
    List<String> genre_names,
    double user_rating,
    Integer critic_score,
    String us_rating,
    String poster,
    String backdrop,
    String original_language,
    List<Long> similar_titles,
    List<Long> networks,
    List<String> network_names,
    String trailer,
    String trailer_thumbnail,
    double relevance_percentile,
    // TODO: This isn't working as expected
    List<WatchModeSource> sources
) {}
