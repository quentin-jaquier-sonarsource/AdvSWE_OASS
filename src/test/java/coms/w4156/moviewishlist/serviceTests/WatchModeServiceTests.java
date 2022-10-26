package coms.w4156.moviewishlist.serviceTests;

import coms.w4156.moviewishlist.services.Source;
import coms.w4156.moviewishlist.services.WatchModeService;
import coms.w4156.moviewishlist.utils.Config;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class WatchModeServiceTests {

    private Config config = new Config();

    private final String skyfallId = "1350564";
    private final String watchmodeUrl = String.format(
        "https://api.watchmode.com/v1/title/%s/sources/?apiKey=%s",
        skyfallId,
        config.getApikey()
    );

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
        return Stream.concat(Arrays.stream(arr1), Arrays.stream(arr2))
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
        netflix.setName("Netflix");

        amazonPrime = new Source();
        amazonPrime.setName("Amazon Prime");

        hulu = new Source();
        hulu.setName("Hulu");

        shudder = new Source();
        shudder.setName("Shudder");

        allSub = new Source[]{netflix, amazonPrime, hulu, shudder};

        for (Source subService : allSub) {
            subService.setType("sub");
        }

        // End init'ing all services that are subscription based

        // Begin init'ing all services for buying movies

        vuduBuy = new Source();
        vuduBuy.setName("VUDU- to buy");

        appleTVBuy = new Source();
        appleTVBuy.setName("Apple TV - to buy");

        amazonVideoBuy = new Source();
        amazonVideoBuy.setName("Amazon Video - to buy");

        iTunesBuy = new Source();
        iTunesBuy.setName("iTunes - to buy");

        allBuy = new Source[]{vuduBuy, appleTVBuy, amazonVideoBuy, iTunesBuy};

        for (Source buyService : allBuy) {
            buyService.setType("buy");
        }

        // End init'ing all services for buying movies

        // End init'ing all services for renting  movies

        vuduRent = new Source();
        vuduRent.setName("VUDU- to rent");

        appleTVRent = new Source();
        appleTVRent.setName("Apple TV - to rent");

        amazonVideoRent = new Source();
        amazonVideoRent.setName("Amazon Video - to rent");

        iTunesRent = new Source();
        iTunesRent.setName("iTunes - to rent");

        allRent = new Source[] {
            vuduRent,
            appleTVRent,
            amazonVideoRent,
            iTunesRent
        };

        for (Source rentService : allRent) {
            rentService.setType("rent");
        }
        // End init'ing all services for renting movies

        rentAndBuySources = concatArrays(allBuy, allRent);

        allSources = concatArrays(rentAndBuySources, allSub);

        // Abstract this out into a method and create an array for all sources.
    }

    /**
     * Tests that the getSources method returns the correct sources for a movie
     * that is available on all services.
     */
    @Test
    public void testAvailable() {
        Source netflix = new Source();
        netflix.setName("Netflix");
        netflix.setType("sub");

        Source amazon = new Source();
        amazon.setName("Amazon");
        amazon.setType("sub");

        Source vudu = new Source();
        vudu.setName("VUDU");
        vudu.setType("buy");

        Source[] allSources = new Source[]{netflix, amazon, vudu};

        Mockito
                .when(restTemplate.getForEntity(watchmodeUrl, Source[].class))
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

        Source[] movie1Sources = new Source[] {netflix, vuduBuy};
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

}
