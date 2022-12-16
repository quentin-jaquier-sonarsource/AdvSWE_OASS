package coms.w4156.moviewishlist.serviceTests;

import static coms.w4156.moviewishlist.utils.StreamingConstants.*;

import coms.w4156.moviewishlist.models.watchMode.TitleDetail;
import coms.w4156.moviewishlist.models.watchMode.TitleSearchResponse;
import coms.w4156.moviewishlist.models.watchMode.TitleSearchResult;
import coms.w4156.moviewishlist.models.watchMode.WatchModeNetwork;
import coms.w4156.moviewishlist.services.Source;
import coms.w4156.moviewishlist.services.WatchModeService;
import java.lang.reflect.Array;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
public class WatchModeServiceTests {
    private final String skyfallId = "1350564";

    private static Source netflix;
    private static Source amazonPrime;
    private static Source hulu;
    private static Source shudder;

    private static Source vuduBuy;
    private static Source vuduRent;
    private static Source appleTVBuy;
    private static Source appleTVRent;

    private static Source amazonVideoBuy;
    private static Source amazonVideoRent;
    private static Source iTunesBuy;
    private static Source iTunesRent;

    private static Source[] allSub;
    private static Source[] allRent;
    private static Source[] allBuy;
    private static Source[] rentAndBuySources;
    private static Source[] allSources;

    private static TitleDetail matrixTitleDetail;

    private static WatchModeNetwork hboNetwork;
    private static WatchModeNetwork shoNetwork;
    private static WatchModeNetwork[] allNetworks;

    private static TitleSearchResult titleSearchResult;
    private static List<TitleSearchResult> titleSearchResultList;

    private static TitleSearchResponse matrixTitleSearchResponse;

    @Value("${apiKey}")
    private String apiKey;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WatchModeService wms = new WatchModeService();

    /**
     * Returns the concatenation of two Source arrays.
     * @param arr1 first Source array
     * @param arr2 second Source array
     * @return the concatenation
     */
    private static Source[] concatArrays(
        final Source[] arr1,
        final Source[] arr2
    ) {
        return Stream
            .concat(Arrays.stream(arr1), Arrays.stream(arr2))
            .toArray(size ->
                (Source[]) Array.newInstance(
                    arr1.getClass().getComponentType(),
                    size
                )
            );
    }

    /**
     * Sets up the mock data for testing.
     */
    @BeforeAll
    public static void setUp() {
        // Init all services that subscription based.

        netflix = new Source(56, NETFLIX_NAME, SUBSCRIPTION_TYPE,
                "US", "ios", "androidUrl", "webUrl",
                "Format:HD", 20.0, 4, 40);

        amazonPrime = new Source();
        amazonPrime.setName(AMAZON_PRIME_NAME);

        hulu = new Source();
        hulu.setName(HULU_NAME);

        shudder = new Source();
        shudder.setName(SHUDDER_NAME);

        allSub = new Source[] { netflix, amazonPrime, hulu, shudder };

        for (Source subService : allSub) {
            subService.setType(SUBSCRIPTION_TYPE);
        }

        // End init'ing all services that are subscription based

        // Begin init'ing all services for buying movies

        vuduBuy = new Source();
        vuduBuy.setName(VUDU_NAME);

        appleTVBuy = new Source();
        appleTVBuy.setName(APPLE_TV_NAME);

        amazonVideoBuy = new Source();
        amazonVideoBuy.setName(AMAZON_VIDEO_NAME);

        iTunesBuy = new Source();
        iTunesBuy.setName(ITUNES_NAME);

        allBuy =
            new Source[] { vuduBuy, appleTVBuy, amazonVideoBuy, iTunesBuy };

        for (Source buyService : allBuy) {
            buyService.setType(BUY_TYPE);
        }

        // End init'ing all services for buying movies

        // End init'ing all services for renting  movies

        vuduRent = new Source();
        vuduRent.setName(VUDU_NAME);

        appleTVRent = new Source();
        appleTVRent.setName(APPLE_TV_NAME);

        amazonVideoRent = new Source();
        amazonVideoRent.setName(AMAZON_VIDEO_NAME);

        iTunesRent = new Source();
        iTunesRent.setName(ITUNES_NAME);

        allRent =
            new Source[] { vuduRent, appleTVRent, amazonVideoRent, iTunesRent };

        for (Source rentService : allRent) {
            rentService.setType(RENT_TYPE);
        }
        // End init'ing all services for renting movies

        rentAndBuySources = concatArrays(allBuy, allRent);

        allSources = concatArrays(rentAndBuySources, allSub);

        allSources = concatArrays(rentAndBuySources, allSub);

        // Start title detail
        matrixTitleDetail = new TitleDetail();
        matrixTitleDetail.setTitle("The Matrix");

        // Setup the networks
        hboNetwork = new WatchModeNetwork(72, "HBO", "USA", 27);
        shoNetwork = new WatchModeNetwork(141, "SHO", "USA", 141);
        allNetworks = new WatchModeNetwork[] {hboNetwork, shoNetwork};

        titleSearchResult = new TitleSearchResult();
        titleSearchResult.setId(56L);
        titleSearchResult.setName("The Matrix");
        titleSearchResult.setYear(1999);
        titleSearchResult.setTmdbId(43L);
        titleSearchResult.setType("Buy");
        titleSearchResult.setRelevance(9.99);
        titleSearchResult.setTmdbType("Blue");
        titleSearchResult.setImageUrl("imageurl");

        titleSearchResultList = new ArrayList<>();
        titleSearchResultList.add(titleSearchResult);
        matrixTitleSearchResponse = new TitleSearchResponse(titleSearchResultList);

    }

    /**
     * Tests that the getSources method returns the correct sources for a movie
     * that is available on all services.
     */
    @Test
    void testAvailable() {
        Source[] allSources = new Source[] { netflix, amazonPrime, vuduRent };

        Mockito
            .when(restTemplate.getForEntity(getWatchmode_url(), Source[].class))
            .thenReturn(new ResponseEntity<>(allSources, HttpStatus.OK));

        List<String> returnedSources = wms.testResponse();

        Assertions.assertEquals(2, returnedSources.size());
    }

    /**
     * Test that we can filter at least one not free source.
     */
    @Test
    void testGetFreeWithSubSourcesById() {
        // Let's say that movie 1 is available to buy on Vudu and to stream on
        // Netflix

        Source[] movie1Sources = new Source[] { netflix, vuduBuy };
        String movie1URL = wms.makeURL("1");

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(
                new ResponseEntity<Source[]>(movie1Sources, HttpStatus.OK)
            );

        List<String> returnedSources = wms.getFreeWithSubSourcesById("1");

        // Assert only 1 source returned
        Assertions.assertEquals(1, returnedSources.size());

        // Assert that source is netflix
        Assertions.assertTrue(returnedSources.contains(netflix.getName()));

        // Assert that the source is NOT vudu
        Assertions.assertFalse(returnedSources.contains(vuduBuy.getName()));
    }

    /**
     * Test that when a movie is only available to rent or buy, this method
     * will return an empty array.
     */
    @Test
    void testGetFreeWithSubSourcesByIdNoneReturned() {
        // Let's say that movie 2 is only available on sources for renting and
        // buying

        Source[] movie2Sources = new Source[] {
            vuduRent,
            vuduBuy,
            iTunesBuy,
            amazonVideoBuy
        };

        String movie2URL = wms.makeURL("2");

        Mockito
            .when(restTemplate.getForEntity(movie2URL, Source[].class))
            .thenReturn(
                new ResponseEntity<Source[]>(movie2Sources, HttpStatus.OK)
            );

        List<String> returnedSources = wms.getFreeWithSubSourcesById("2");

        // Assert no sources expected
        Assertions.assertEquals(0, returnedSources.size());
    }

    /**
     * Test that when this movie is available nowhere, that this method
     * returns an empty array.
     */
    @Test
    void testGetFreeWithSubSourcesByIdNoneReturned2() {
        // Let's say that movie 3 isn't available anywhere.

        Source[] movie3Sources = new Source[] {};
        String movie3URL = wms.makeURL("3");

        Mockito
            .when(restTemplate.getForEntity(movie3URL, Source[].class))
            .thenReturn(
                new ResponseEntity<Source[]>(movie3Sources, HttpStatus.OK)
            );

        List<String> returnedSources = wms.getFreeWithSubSourcesById("3");

        // Assert no sources expected
        Assertions.assertEquals(0, returnedSources.size());
    }

    /**
     * Test that when this movie is available only on all streaming sites,
     * that all streaming sites are returned.
     */
    @Test
    void testGetFreeWithSubSourcesAllSub() {
        String movie3URL = wms.makeURL("3");

        Mockito
            .when(restTemplate.getForEntity(movie3URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(allSub, HttpStatus.OK));

        List<String> returnedSources = wms.getFreeWithSubSourcesById("3");

        // Assert all sources expected
        Assertions.assertEquals(allSub.length, returnedSources.size());

        for (Source subSource : allSub) {
            Assertions.assertTrue(
                returnedSources.contains(subSource.getName())
            );
        }
    }

    /**
     * Test that when this movie is available only on all buy sites,
     * that all buy sites are returned.
     */
    @Test
    void testGetFreeWithSubAllSources() {
        String movie000000URL = wms.makeURL("000000");

        Mockito
            .when(restTemplate.getForEntity(movie000000URL, Source[].class))
            .thenReturn(
                new ResponseEntity<Source[]>(allSources, HttpStatus.OK)
            );

        List<String> returnedSources = wms.getFreeWithSubSourcesById("000000");

        // Assert all sources expected
        Assertions.assertEquals(allSub.length, returnedSources.size());

        for (Source subSource : allSub) {
            Assertions.assertTrue(
                returnedSources.contains(subSource.getName())
            );
        }
    }

    /**
     * Test when a movie is only available to rent or buy on all services where
     * a movie can be rented or bought.
     */
    @Test
    void testGetFreeWithSubAllRentBuy() {
        String movie000000URL = wms.makeURL("000000");

        Mockito
            .when(restTemplate.getForEntity(movie000000URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(allRent, HttpStatus.OK));

        List<String> returnedSources = wms.getFreeWithSubSourcesById("000000");

        // Movie cannot be streamed for free
        Assertions.assertEquals(0, returnedSources.size());
    }

    /************End Test getFreeWithSubscription function **************/

    public String getWatchmode_url() {
        return (
            "https://api.watchmode.com/v1/title/" +
            skyfallId +
            "/sources/?apiKey=" +
            apiKey
        );
    }

    /********************Test getBuySourcesById function***********************/

    /**
     * Test that we can filter at least one not buy source
     */
    @Test
    void testGetBuySourcesById() {
        /** Let's say that movie 1 is available to buy on Vudu and to stream on
         * Netflix
         */

        Source[] movie1Sources = new Source[] { netflix, vuduBuy };
        String movie1URL = wms.makeURL("1");

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(movie1Sources, HttpStatus.OK));

        List<String> returnedSources = wms.getBuySourcesById("1");

        // Assert only 1 source returned
        Assertions.assertEquals(1, returnedSources.size());

        // Assert that source is Vudu
        Assertions.assertTrue(returnedSources.contains(vuduBuy.getName()));

        // Assert that the source is NOT netflix
        Assertions.assertFalse(returnedSources.contains(netflix.getName()));
    }

    /**
     * Test that we can filter when all sources are of type buy
     */
    @Test
    void testGetBuySourcesByIdAllBuy() {
        /** Let's say that movie 1 is available only to buy
         */

        String movie1URL = wms.makeURL("1");

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(allBuy, HttpStatus.OK));

        List<String> returnedSources = wms.getBuySourcesById("1");

        // Assert only 1 source returned
        Assertions.assertEquals(allBuy.length, returnedSources.size());

        // Assert that all the 'buy' sources are in there
        for (Source buySource : allBuy) {
            Assertions.assertTrue(
                returnedSources.contains(buySource.getName())
            );
        }
    }

    /**
     * Test that we can filter when no sources are of type buy
     */
    @Test
    void testGetBuySourcesByIdNoBuy() {
        /** Let's say that movie 1 is available only to stream on subscription
         * platforms */

        String movie1URL = wms.makeURL("1");

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(allSub, HttpStatus.OK));

        List<String> returnedSources = wms.getBuySourcesById("1");

        // Assert only 1 source returned
        Assertions.assertEquals(0, returnedSources.size());
    }

    /**
     * Test that we can filter when movie isn't available anywhere
     */
    @Test
    void testGetBuySourcesByIdNoBuy2() {
        Source[] noSources = {};
        String movie1URL = wms.makeURL("1");

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(noSources, HttpStatus.OK));

        List<String> returnedSources = wms.getBuySourcesById("1");

        // Assert only no source returned
        Assertions.assertEquals(0, returnedSources.size());
    }

    /**
     * Test that we can filter buy sources from all sources
     */
    @Test
    void testGetBuySourcesByIdAllSources() {
        /** Let's say that movie 1 is available only to buy
         */

        String movie1URL = wms.makeURL("1");

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(allSources, HttpStatus.OK));

        List<String> returnedSources = wms.getBuySourcesById("1");

        // Assert only 1 source returned
        Assertions.assertEquals(allBuy.length, returnedSources.size());

        // Assert that all the 'buy' sources are in there
        for (Source buySource : allBuy) {
            Assertions.assertTrue(
                returnedSources.contains(buySource.getName())
            );
        }
    }

    /*****************END Test getBuySourcesById function**********************/

    /******************Test getRentSourcesById function************************/

    /**
     * Test that we can filter at least one not buy source
     */
    @Test
    void testGetRentSourcesById() {
        /** Let's say that movie 1 is available to rent on Vudu and to stream on
         * Netflix
         */

        Source[] movie1Sources = new Source[] { netflix, vuduRent };
        String movie1URL = wms.makeURL("1");

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(movie1Sources, HttpStatus.OK));

        List<String> returnedSources = wms.getRentSourcesById("1");

        // Assert only 1 source returned
        Assertions.assertEquals(1, returnedSources.size());

        // Assert that source is Vudu
        Assertions.assertTrue(returnedSources.contains(vuduRent.getName()));

        // Assert that the source is NOT netflix
        Assertions.assertFalse(returnedSources.contains(netflix.getName()));
    }

    /**
     * Test that we can filter when all sources are of type Rent
     */
    @Test
    void testGetRentSourcesByIdAllRent() {
        /** Let's say that movie 1 is available only to Rent
         */

        String movie1URL = wms.makeURL("1");

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(allRent, HttpStatus.OK));

        List<String> returnedSources = wms.getRentSourcesById("1");

        // Assert only 1 source returned
        Assertions.assertEquals(allRent.length, returnedSources.size());

        // Assert that all the 'Rent' sources are in there
        for (Source buySource : allRent) {
            Assertions.assertTrue(
                returnedSources.contains(buySource.getName())
            );
        }
    }

    /**
     * Test that we can filter when no sources are of type Rent
     */
    @Test
    void testGetRentSourcesByIdNoRent() {
        /** Let's say that movie 1 is available only to stream on subscription
         * platforms */

        String movie1URL = wms.makeURL("1");

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(allSub, HttpStatus.OK));

        List<String> returnedSources = wms.getRentSourcesById("1");

        // Assert only 1 source returned
        Assertions.assertEquals(0, returnedSources.size());
    }

    /**
     * Test that we can filter when movie isn't available anywhere
     */
    @Test
    void testGetRentSourcesByIdNoRent2() {
        Source[] noSources = {};
        String movie1URL = wms.makeURL("1");

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(noSources, HttpStatus.OK));

        List<String> returnedSources = wms.getRentSourcesById("1");

        // Assert only no source returned
        Assertions.assertEquals(0, returnedSources.size());
    }

    /**
     * Test that we can filter Rent sources from all sources
     */
    @Test
    void testGetRentSourcesByIdAllSources() {
        /** Let's say that movie 1 is available only to buy
         */

        String movie1URL = wms.makeURL("1");

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(allSources, HttpStatus.OK));

        List<String> returnedSources = wms.getRentSourcesById("1");

        Assertions.assertEquals(allRent.length, returnedSources.size());

        // Assert that all the 'Rent' sources are in there
        for (Source buySource : allRent) {
            Assertions.assertTrue(
                returnedSources.contains(buySource.getName())
            );
        }
    }

    /****************END Test getRentSourcesById function**********************/

    /******************START Test Uniqueness of Sources************************/
    @Test
    void testGetFreeWithSubSourcesByIdRepeat() {
        /** Let's say that movie 1 is available only to buy
         */

        String movie1URL = wms.makeURL("1");

        Source[] repeatedSources = concatArrays(allSources, allSources);

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(repeatedSources, HttpStatus.OK));

        List<String> returnedSources = wms.getFreeWithSubSourcesById("1");

        Assertions.assertEquals(allSub.length, returnedSources.size());

        // Assert that all the 'Rent' sources are in there
        for (Source buySource : allSub) {
            Assertions.assertTrue(
                returnedSources.contains(buySource.getName())
            );
        }
    }

    @Test
    void testGetRentSourcesByIdRepeat() {
        /** Let's say that movie 1 is available only to buy
         */

        String movie1URL = wms.makeURL("1");

        Source[] repeatedSources = concatArrays(allSources, allSources);

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(repeatedSources, HttpStatus.OK));

        List<String> returnedSources = wms.getRentSourcesById("1");

        Assertions.assertEquals(allRent.length, returnedSources.size());

        // Assert that all the 'Rent' sources are in there
        for (Source buySource : allRent) {
            Assertions.assertTrue(
                returnedSources.contains(buySource.getName())
            );
        }
    }

    @Test
    void testGetBuySourcesByIdRepeat() {
        /** Let's say that movie 1 is available only to buy
         */

        String movie1URL = wms.makeURL("1");

        Source[] repeatedSources = concatArrays(allSources, allSources);

        Mockito
            .when(restTemplate.getForEntity(movie1URL, Source[].class))
            .thenReturn(new ResponseEntity<Source[]>(repeatedSources, HttpStatus.OK));

        List<String> returnedSources = wms.getBuySourcesById("1");

        Assertions.assertEquals(allBuy.length, returnedSources.size());

        // Assert that all the 'Rent' sources are in there
        for (Source buySource : allBuy) {
            Assertions.assertTrue(
                returnedSources.contains(buySource.getName())
            );
        }
    }
    /*******************END Test Uniqueness of Sources*************************/

    /******************START TITLE DETAIL TESTS********************************/
    @Test
    void testTitleDetailNoSources() {
        /** Let's say that movie 1 is available only to buy
         */
        String titleId = "72";
        Boolean includeSources = false;

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(WatchModeService.WATCHMODE_API_BASE_URL)
                .pathSegment("title", titleId, "details")
                .queryParam("apiKey", apiKey);

        var uri = builder.build().toUri();

        Mockito
                .when(restTemplate.getForEntity(uri, TitleDetail.class))
                .thenReturn(new ResponseEntity<TitleDetail>(matrixTitleDetail, HttpStatus.OK));

        TitleDetail returnedDetail = wms.getTitleDetail(titleId, includeSources);

        Assertions.assertEquals(returnedDetail, matrixTitleDetail);
    }

    @Test
    void testTitleDetailNullIncludeSources() {
        // Should function the same as above now that we check for nulls
        String titleId = "72";
        Boolean includeSources = null;

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(WatchModeService.WATCHMODE_API_BASE_URL)
                .pathSegment("title", titleId, "details")
                .queryParam("apiKey", apiKey);

        var uri = builder.build().toUri();

        Mockito
                .when(restTemplate.getForEntity(uri, TitleDetail.class))
                .thenReturn(new ResponseEntity<TitleDetail>(matrixTitleDetail, HttpStatus.OK));

        TitleDetail returnedDetail = wms.getTitleDetail(titleId, includeSources);

        Assertions.assertEquals(returnedDetail, matrixTitleDetail);
    }

    @Test
    void testTitleDetailIncludeSources() {
        // Check we wired up the true case to work properly as well
        String titleId = "72";
        Boolean includeSources = true;

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(WatchModeService.WATCHMODE_API_BASE_URL)
                .pathSegment("title", titleId, "details")
                .queryParam("apiKey", apiKey);
        builder = builder.queryParam("append_to_response", "sources");

        var uri = builder.build().toUri();

        Mockito
                .when(restTemplate.getForEntity(uri, TitleDetail.class))
                .thenReturn(new ResponseEntity<TitleDetail>(matrixTitleDetail, HttpStatus.OK));

        TitleDetail returnedDetail = wms.getTitleDetail(titleId, includeSources);

        Assertions.assertEquals(returnedDetail, matrixTitleDetail);
    }

    @Test
    void testGetTitlesBySearch() {
        String searchQuery = "Matrix";
        URI uri = UriComponentsBuilder
                .fromHttpUrl(WatchModeService.WATCHMODE_API_BASE_URL)
                .pathSegment("autocomplete-search")
                .queryParam("apiKey", apiKey)
                .queryParam("search_value", searchQuery)
                .queryParam("search_type", 2)
                .build()
                .toUri();

        Mockito
                .when(restTemplate.getForEntity(uri, TitleSearchResponse.class))
                .thenReturn(new ResponseEntity<TitleSearchResponse>(
                        matrixTitleSearchResponse, HttpStatus.OK));

        TitleSearchResponse returnedResponse = wms.getTitlesBySearch(searchQuery);

        Assertions.assertEquals(matrixTitleSearchResponse, returnedResponse);
    }

    @Test
    void testGetAllNetworks() {
        URI uri = UriComponentsBuilder
                .fromHttpUrl(WatchModeService.WATCHMODE_API_BASE_URL)
                .pathSegment("networks")
                .queryParam("apiKey", apiKey)
                .queryParam("regions", "US")
                .build()
                .toUri();


        Mockito
                .when(restTemplate.getForEntity(uri, WatchModeNetwork[].class))
                .thenReturn(new ResponseEntity<WatchModeNetwork[]>(allNetworks, HttpStatus.OK));

        List<WatchModeNetwork> returnedNetworks = wms.getAllNetworks();
        List<WatchModeNetwork> origNetworks = Arrays.asList(allNetworks);

        // Assert that they are subsets of each other and are the same size
        // Need the size constraint because they are not sets but are lists
        Assertions.assertEquals(returnedNetworks.size(), origNetworks.size());
        Assertions.assertTrue(returnedNetworks.containsAll(origNetworks));
        Assertions.assertTrue(origNetworks.containsAll(returnedNetworks));
    }
}
