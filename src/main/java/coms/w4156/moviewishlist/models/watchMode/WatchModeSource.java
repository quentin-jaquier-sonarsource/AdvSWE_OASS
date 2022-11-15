package coms.w4156.moviewishlist.models.watchMode;

import java.util.List;
import java.util.Optional;

public record WatchModeSource(
    Optional<Long> id,
    Optional<Long> source_id,
    String name,
    String type,
    String logo_100px,
    String ios_appstore_url,
    String android_playstore_url,
    String android_scheme,
    String ios_scheme,
    List<String> regions
) {}
