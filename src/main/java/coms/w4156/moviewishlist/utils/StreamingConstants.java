package coms.w4156.moviewishlist.utils;

public final class StreamingConstants {

    private StreamingConstants() {}

    /*                      DEFINE SOURCE TYPES                               */

    /** WatchMode type for services that are subscription based. */
    public static final String SUBSCRIPTION_TYPE = "sub";

    /** WatchMode type for services where you rent a movie. */
    public static final String RENT_TYPE = "rent";

    /** WatchMode type for services where you buy a movie. */
    public static final String BUY_TYPE = "buy";

    /*                    END DEFINE SOURCE TYPES                             */

    /*                      DEFINE SOURCE NAMES                               */

    /** Netflix.*/
    public static final String NETFLIX_NAME = "Netflix";

    /** Amazon Prime.*/
    public static final String AMAZON_PRIME_NAME = "Amazon Prime";

    /** Hulu.*/
    public static final String HULU_NAME = "Hulu";

    /** HBO Max.*/
    public static final String HBO_NAME = "HBO Max";

    /** Shudder.*/
    public static final String SHUDDER_NAME = "Shudder";

    /** Vudu.*/
    public static final String VUDU_NAME = "Vudu";

    /** Apple TV.*/
    public static final String APPLE_TV_NAME = "Apple TV";

    /** Amazon Video.*/
    public static final String AMAZON_VIDEO_NAME = "Amazon Video";

    /** iTunes.*/
    public static final String ITUNES_NAME = "iTunes";

    /*                    END DEFINE SOURCE NAMES                             */

    /*                    DEFINE SOURCE ENDPOINTS                             */

    /** The base prefix for every endpoint in the streaming controller.*/
    public static final String STREAMING_BASE = "/streaming";

    /** Relative endpoint of the streaming test endpoint.*/
    public static final String STREAMING_TEST_ENDPOINT = "/available";

    /** Relative endpoint of the subscription by id endpoint.*/
    public static final String STREAMING_SUB_ENDPOINT = "/freeWithSub";

    /** Relative endpoint of the buy by id endpoint.*/
    public static final String STREAMING_BUY_ENDPOINT = "/availToBuy";

    /** Relative endpoint of the rent by id endpoint.*/
    public static final String STREAMING_RENT_ENDPOINT = "/availToRent";
    /*                  END DEFINE SOURCE ENDPOINTS                           */

}
