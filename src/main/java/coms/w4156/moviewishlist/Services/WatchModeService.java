package coms.w4156.moviewishlist.Services;

import coms.w4156.moviewishlist.utils.Config;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to abstract away the logic related to getting information from the WatchMode API
 */
@Service
public class WatchModeService {

    Config config = new Config();
    private final String test_host = "https://yesno.wtf/api";
    private final String parasiteId = "1295258";
    private final String elCaminoId = "1586594 ";
    private final String hostID = "1616666 ";
    private final String skyfallId = "1350564";
    private final String annhilationId = "130381";

    private final String watchModeSourceBaseEndpoint = "https://api.watchmode.com/v1/title/";
    private final String watchmode_url = "https://api.watchmode.com/v1/title/"+skyfallId+"/sources/?apiKey="+config.getAPIKey();
    private final String charset = "UTF-8";
    String mode;

    public WatchModeService() {
        this.mode = "test";
    }

    public WatchModeService(String mode) {
        this.mode = mode;
    }


    RestTemplate restTemplate = new RestTemplate();


    /**
     * A test response that fetches a hardcoded movie.
     * @return response from the api
     */
    public String[] testResponse() {

        if (this.mode == "test") {
            YesNo result = restTemplate.getForObject(test_host, YesNo.class);
            return new String[] {result.getAnswer()};
        } else {
            ResponseEntity<Source[]> responseEntity = restTemplate.getForEntity(watchmode_url, Source[].class);

            Source[] sources = responseEntity.getBody();

            List<String> sourceNameList = Arrays.stream(sources)
                    .filter(Source::isFreeWithSubscription)
                    .map(Source::getName)
                    .collect(Collectors.toList());

            return sourceNameList.toArray(new String[0]);
        }
    }

    /**
     * Function to return a list of sources that are free with subscription by
     * the Watchmode ID.
     *
     * @param watchModeID the ID for the movie in the watchmode API
     * @return an array of Strings which are the source names
     */
    public String[] getFreeWithSubSourcesById(final String watchModeID) {

        Source[] allSources = getSources(watchModeID);

        String[] filteredSources = Arrays.stream(allSources)
                .filter(Source::isFreeWithSubscription)
                .map(Source::getName)
                .collect(Collectors.toList())
                .toArray(new String[0]);

        return filteredSources;

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

        ResponseEntity<Source[]> responseEntity = restTemplate.getForEntity(url,
                Source[].class);

        Source[] sources = responseEntity.getBody();

        return sources;
    }

    private String makeURL(final String watchModeID) {
        return watchModeSourceBaseEndpoint + watchModeID
                + "/sources/?apiKey=" + config.getAPIKey();
    }


}
