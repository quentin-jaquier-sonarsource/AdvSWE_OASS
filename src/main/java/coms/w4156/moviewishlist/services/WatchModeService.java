package coms.w4156.moviewishlist.services;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to abstract away the logic related to getting information from
 * the WatchMode API.
 */
@NoArgsConstructor @Getter @Setter
@Service
public class WatchModeService {


    /**
     * A test ID for the movie Skyfall.
     */
    private final String skyfallId = "1350564";

    /**
     * The base endpoint for making queries about movie sources.
     */
    private final String watchModeSourceBaseEndpoint =
            "https://api.watchmode.com/v1/title/";

    /**
     * A test endpoint call to see what sources the movie Skyfall is available
     * on.
     */

    @Value("${apiKey}")
    private String apiKey;


    /**
     * The character set for making API queries.
     */
    private final String charset = "UTF-8";

    /**
     * RestTemplate used for hitting the API endpoints.
     *
     * Should we use "Spring 5 WebClient" instead?
     * https://www.baeldung.com/spring-5-webclient
     */
    private RestTemplate restTemplate = new RestTemplate();


    /**
     * A test response that fetches a hardcoded movie.
     * @return response from the api
     */
    public List<String> testResponse() {

        ResponseEntity<Source[]> responseEntity = restTemplate
                .getForEntity(getWatchmodeTestURL(), Source[].class);

        Source[] sources = responseEntity.getBody();

        return Arrays.stream(sources)
                .filter(Source::isFreeWithSubscription)
                .map(Source::getName)
                .collect(Collectors.toList());
    }


    /**
     * Function to return a list of sources that are free with subscription by
     * the WatchMode ID.
     *
     * @param watchModeID the ID for the movie in the WatchMode API
     * @return an array of Strings which are the source names
     */
    public List<String> getFreeWithSubSourcesById(final String watchModeID) {

        Source[] allSources = getSources(watchModeID);

        return Arrays.stream(allSources)
                .filter(Source::isFreeWithSubscription)
                .map(Source::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Function to return a list of sources that rent the given movie by defined
     * by the WatchMode ID.
     *
     * @param watchModeID the ID for the movie in the WatchMode API
     * @return an array of Strings which are the source names
     */
    public List<String> getRentSourcesById(final String watchModeID) {

        Source[] allSources = getSources(watchModeID);

        return Arrays.stream(allSources)
                .filter(Source::isRentSource)
                .map(Source::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Function to return a list of sources that sell the given movie defined by
     * the WatchMode ID.
     *
     * @param watchModeID the ID for the movie in the WatchMode API
     * @return an array of Strings which are the source names
     */
    public List<String> getBuySourcesById(final String watchModeID) {

        Source[] allSources = getSources(watchModeID);

        return Arrays.stream(allSources)
                .filter(Source::isBuySource)
                .map(Source::getName)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Gets all the Souce objects for the given movie id. This includes sources
     * for buying and renting and well as those that stream free with s
     * subscription.
     *
     * @param watchModeID the ID for the movie in the Watchmode API
     * @return the entire array of Source objects returned by the API
     */
    public Source[] getSources(final String watchModeID) {
        String url = makeURL(watchModeID);

        return restTemplate
            .getForEntity(url, Source[].class)
            .getBody();
    }

    /**
     * Creates a url to query WatchMode given a WatchMode movie id.
     * @param watchModeID the id we want to query about its sources.
     * @return A url to query.
     */
    public String makeURL(final String watchModeID) {
        return watchModeSourceBaseEndpoint + watchModeID
                + "/sources/?apiKey=" + apiKey;
    }


    public String getWatchmodeTestURL() {
        return "https://api.watchmode.com/v1/title/" + skyfallId
                + "/sources/?apiKey=" + apiKey;
    }
}
