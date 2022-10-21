package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.utils.Config;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
     * The config object for getting the API key.
     */
    private Config config = new Config();

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
    private final String watchmodeTestURL =
            "https://api.watchmode.com/v1/title/" + skyfallId
                    + "/sources/?apiKey=" + config.getApikey();

    /**
     * The character set for making API queries.
     */
    private final String charset = "UTF-8";

    /**
     * RestTemplate used for hitting the API endpoints.
     * TODO: Should we use "Spring 5 WebClient" instead?
     * https://www.baeldung.com/spring-5-webclient
     */
    private RestTemplate restTemplate = new RestTemplate();


    /**
     * A test response that fetches a hardcoded movie.
     * @return response from the api
     */
    public List<String> testResponse() {

        ResponseEntity<Source[]> responseEntity = restTemplate
                .getForEntity(watchmodeTestURL, Source[].class);

        Source[] sources = responseEntity.getBody();

        return Arrays.stream(sources)
                .filter(Source::isFreeWithSubscription)
                .map(Source::getName)
                .collect(Collectors.toList());
    }


    /**
     * Function to return a list of sources that are free with subscription by
     * the Watchmode ID.
     *
     * @param watchModeID the ID for the movie in the watchmode API
     * @return an array of Strings which are the source names
     */
    public List<String> getFreeWithSubSourcesById(final String watchModeID) {

        Source[] allSources = getSources(watchModeID);

        return Arrays.stream(allSources)
                .filter(Source::isFreeWithSubscription)
                .map(Source::getName)
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
    private Source[] getSources(final String watchModeID) {
        String url = makeURL(watchModeID);

        return restTemplate
            .getForEntity(url, Source[].class)
            .getBody();
    }

    private String makeURL(final String watchModeID) {
        return watchModeSourceBaseEndpoint + watchModeID
                + "/sources/?apiKey=" + config.getApikey();
    }


}
