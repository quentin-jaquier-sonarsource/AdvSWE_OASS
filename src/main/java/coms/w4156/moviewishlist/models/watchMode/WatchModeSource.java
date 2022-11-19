package coms.w4156.moviewishlist.models.watchMode;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Optional;
import lombok.Data;

@Data
public class WatchModeSource {

    @JsonAlias("source_id")
    private Long id;

    private String name;

    private String type;

    private String region;

    @JsonAlias("ios_url")
    private String iosUrl;

    @JsonAlias("android_url")
    private String androidUrl;

    @JsonAlias("web_url")
    private String webUrl;

    private String format;

    private String price; // Float?

    private Optional<Integer> seasons;

    private Optional<Integer> episodes;
}
