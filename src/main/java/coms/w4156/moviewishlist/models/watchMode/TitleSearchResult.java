package coms.w4156.moviewishlist.models.watchMode;

public record TitleSearchResult(
    String name,
    double relevance,
    String type,
    int id,
    int year,
    String result_type,
    int tmdb_id,
    String tmdb_type,
    String image_url
) {}
