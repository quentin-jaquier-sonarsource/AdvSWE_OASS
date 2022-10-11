package coms.w4156.moviewishlist.Services;

import lombok.*;

/**
 * Class to represent a WatchMode streaming source
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Source {
    private int source_id;
    private String name;
    private String type;
    private String region;
    private String ios_url;
    private String android_url;
    private String web_url;
    private String format;
    private double price;
    private int seasons;
    private int episodes;

    public boolean isFreeWithSubscription() {
        return this.type.equals("sub");
    }
}