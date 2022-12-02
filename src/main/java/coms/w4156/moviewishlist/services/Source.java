package coms.w4156.moviewishlist.services;

import static coms.w4156.moviewishlist.utils.StreamingConstants.BUY_TYPE;
import static coms.w4156.moviewishlist.utils.StreamingConstants.RENT_TYPE;
import static coms.w4156.moviewishlist.utils.StreamingConstants.SUBSCRIPTION_TYPE;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Class to represent a WatchMode streaming source. A streaming source can be
 * something like Netflix, VUDU, AppleTV, Amazon Prime, etc. A movie is either
 * available for free on a source (so long as you have bought a subscription to
 * that streaming service) or it could be available to buy or rent.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Source {

    /** The WatchMode given id for the source. */
    private int sourceId;

    /** The name of the source. E.g. Netflix. */
    private String name;

    /** The type of the source. E.g. buy, rent, sub.*/
    private String type;

    /** The region in which this movie is available on this source.*/
    private String region;

    /** URL to the source on the Apple App Store.*/
    private String iosUrl;

    /** URL to this source on android.*/
    private String androidUrl;

    /** General browser URL to this movie.*/
    private String webUrl;

    /** */
    private String format;

    /** If type==rent || type==buy, this is the price.*/
    private double price;

    /** Only need if we're considering TV shows. Out of scope for this project.
     * But nevertheless it is returned by the API.
     */
    private int seasons;

    /** Ditto above comment.*/
    private int episodes;

    /**
     * This function is designed to be used with the filter function.
     * @return True if the given source is subscription based.
     */
    public boolean isFreeWithSubscription() {
        return this.type.equals(SUBSCRIPTION_TYPE);
    }

    /**
     * This function is designed to be used with the filter function.
     * @return True if the given source is one where you rent movies.
     */
    public boolean isRentSource() {
        return this.type.equals(RENT_TYPE);
    }

    /**
     * This function is designed to be used with the filter function.
     * @return True if the given source is one where you buy movies.
     */
    public boolean isBuySource() {
        return this.type.equals(BUY_TYPE);
    }
}
