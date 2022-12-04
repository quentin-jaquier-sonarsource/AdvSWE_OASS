package coms.w4156.moviewishlist.serviceTests;

import static coms.w4156.moviewishlist.utils.StreamingConstants.*;

import coms.w4156.moviewishlist.services.Source;
import coms.w4156.moviewishlist.services.WatchModeService;
import java.lang.reflect.Array;
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

        netflix = new Source();
        netflix.setName(NETFLIX_NAME);

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
    }

    /**
     * Tests that the getSources method returns the correct sources for a movie
     * that is available on all services.
     */
    @Test
    public void testAvailable() {
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
    public void testGetFreeWithSubSourcesById() {
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
    public void testGetFreeWithSubSourcesByIdNoneReturned() {
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
    public void testGetFreeWithSubSourcesByIdNoneReturned2() {
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
    public void testGetFreeWithSubSourcesAllSub() {
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
    public void testGetFreeWithSubAllSources() {
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
    public void testGetFreeWithSubAllRentBuy() {
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
    public void testGetBuySourcesById() {
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
    public void testGetBuySourcesByIdAllBuy() {
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
    public void testGetBuySourcesByIdNoBuy() {
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
    public void testGetBuySourcesByIdNoBuy2() {
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
    public void testGetBuySourcesByIdAllSources() {
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
    public void testGetRentSourcesById() {
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
    public void testGetRentSourcesByIdAllRent() {
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
    public void testGetRentSourcesByIdNoRent() {
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
    public void testGetRentSourcesByIdNoRent2() {
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
    public void testGetRentSourcesByIdAllSources() {
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
    public void testGetFreeWithSubSourcesByIdRepeat() {
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
    public void testGetRentSourcesByIdRepeat() {
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
    public void testGetBuySourcesByIdRepeat() {
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
}
