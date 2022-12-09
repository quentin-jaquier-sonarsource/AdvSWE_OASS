package coms.w4156.moviewishlist.services;

import coms.w4156.moviewishlist.models.watchMode.TitleDetail;
import coms.w4156.moviewishlist.models.watchMode.TitleSearchResponse;
import coms.w4156.moviewishlist.models.watchMode.WatchModeNetwork;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Service class to abstract away the logic related to getting information from
 * the WatchMode API.
 */
@NoArgsConstructor
@Getter
@Setter
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
     *
     * @return response from the api
     */
    public List<String> testResponse() {
        ResponseEntity<Source[]> responseEntity = restTemplate.getForEntity(
            getWatchmodeTestURL(),
            Source[].class
        );

        Source[] sources = responseEntity.getBody();

        return Arrays
            .stream(sources)
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

        return Arrays
            .stream(allSources)
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

        return Arrays
            .stream(allSources)
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

        return Arrays
            .stream(allSources)
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

        return restTemplate.getForEntity(url, Source[].class).getBody();
    }

    /**
     * Creates a url to query WatchMode given a WatchMode movie id.
     *
     * @param watchModeID the id we want to query about its sources.
     * @return A url to query.
     */
    public String makeURL(final String watchModeID) {
        return (
            watchModeSourceBaseEndpoint +
            watchModeID +
            "/sources/?apiKey=" +
            apiKey
        );
    }

    /**
     * This method simply returns the URL to test that WatchMode API
     * is working correctly.
     * @return the URL to test the API
     */
    public String getWatchmodeTestURL() {
        return (
            "https://api.watchmode.com/v1/title/" +
            skyfallId +
            "/sources/?apiKey=" +
            apiKey
        );
    }

    //    /**
    //     * Get all sources supported by the WatchMode API.
    //     * @return a list of titles that match the search
    //     */
    //    public List<WatchModeSource> getAllSources() {
    //        URI uri = UriComponentsBuilder
    //            .fromHttpUrl("https://api.watchmode.com/v1/")
    //            .pathSegment("sources")
    //            .queryParam("apiKey", apiKey)
    //            .queryParam("regions", "US")
    //            .build()
    //            .toUri();
    //
    //        WatchModeSource[] sources = restTemplate
    //            .getForEntity(uri, WatchModeSource[].class)
    //            .getBody();
    //
    //        return Arrays.asList(sources);
    //    }

    /**
     * method to search for movies and people by title.
     * @return a list of titles that match the search
     */
    public List<WatchModeNetwork> getAllNetworks() {
        URI uri = UriComponentsBuilder
            .fromHttpUrl("https://api.watchmode.com/v1/")
            .pathSegment("networks")
            .queryParam("apiKey", apiKey)
            .queryParam("regions", "US")
            .build()
            .toUri();

        WatchModeNetwork[] sources = restTemplate
            .getForEntity(uri, WatchModeNetwork[].class)
            .getBody();

        return Arrays.asList(sources);
    }

    /**
     * method to autocomplete search for Titles by title.
     * @param query the title of the movie or person
     * @return a list of titles that match the search
     */
    public TitleSearchResponse getTitlesBySearch(final String query) {
        URI uri = UriComponentsBuilder
            .fromHttpUrl("https://api.watchmode.com/v1/")
            .pathSegment("autocomplete-search")
            .queryParam("apiKey", apiKey)
            .queryParam("search_value", query)
            .queryParam("search_type", 2)
            .build()
            .toUri();

        return restTemplate
            .getForEntity(uri, TitleSearchResponse.class)
            .getBody();
    }

    /**
     * method to get details about a specific title.
     * @param id The id of the title
     * @param includeSources whether or not to include sources
     * @return details about the title
     */
    public TitleDetail getTitleDetail(
        final String id,
        final Boolean includeSources
    ) {
        UriComponentsBuilder builder = UriComponentsBuilder
            .fromHttpUrl("https://api.watchmode.com/v1/")
            .pathSegment("title", id, "details")
            .queryParam("apiKey", apiKey);

        if (includeSources) {
            builder = builder.queryParam("append_to_response", "sources");
        }

        var uri = builder.build().toUri();

        return restTemplate.getForEntity(uri, TitleDetail.class).getBody();
    }
    //    /**
    //     * Get original title of movie by name.
    //     * @param title - the id of the movie
    //     * @return - the movie name
    //     */
    //    public String getMovieName(final String title) {
    //        UriComponentsBuilder builder = UriComponentsBuilder
    //            .fromHttpUrl("https://api.watchmode.com/v1/")
    //            .pathSegment("title", title, "details")
    //            .queryParam("apiKey", apiKey);
    //
    //        var uri = builder.build().toUri();
    //
    //        return restTemplate
    //            .getForEntity(uri, TitleDetail.class)
    //            .getBody()
    //            .getOriginalTitle();
    //    }
    //
    //    /**
    //     * Get release year of the movie.
    //     * @param title - the title of the movie
    //     * @return - the movie release year
    //     */
    //    public String getMovieReleaseYear(final String title) {
    //        int year;
    //        UriComponentsBuilder builder = UriComponentsBuilder
    //            .fromHttpUrl("https://api.watchmode.com/v1/")
    //            .pathSegment("title", title, "details")
    //            .queryParam("apiKey", apiKey);
    //
    //        var uri = builder.build().toUri();
    //
    //        year =
    //            restTemplate
    //                .getForEntity(uri, TitleDetail.class)
    //                .getBody()
    //                .getYear();
    //        return Integer.toString(year);
    //    }
    //
    //    /**
    //     * Get the genre of the movie.
    //     * @param title - the title of the movie
    //     * @return - the movie genre
    //     */
    //    public String getMovieGenre(final String title) {
    //        List<String> genreNames = new ArrayList<>();
    //        UriComponentsBuilder builder = UriComponentsBuilder
    //            .fromHttpUrl("https://api.watchmode.com/v1/")
    //            .pathSegment("title", title, "details")
    //            .queryParam("apiKey", apiKey);
    //
    //        var uri = builder.build().toUri();
    //
    //        genreNames =
    //            restTemplate
    //                .getForEntity(uri, TitleDetail.class)
    //                .getBody()
    //                .getGenreNames();
    //        return genreNames.get(0);
    //    }
    //
    //    /**
    //     * Get the runtime of the movie.
    //     * @param title - the title of the movie
    //     * @return - the movie genre
    //     */
    //    public int getMovieRuntime(final String title) {
    //        UriComponentsBuilder builder = UriComponentsBuilder
    //            .fromHttpUrl("https://api.watchmode.com/v1/")
    //            .pathSegment("title", title, "details")
    //            .queryParam("apiKey", apiKey);
    //
    //        var uri = builder.build().toUri();
    //
    //        return restTemplate
    //            .getForEntity(uri, TitleDetail.class)
    //            .getBody()
    //            .getRuntimeMinutes();
    //    }
    //
    //    /**
    //     * Get the criticScore of the movie.
    //     * @param title - the title of the movie
    //     * @return - the movie genre
    //     */
    //    public int getMoviesByCriticScore(final String title) {
    //        UriComponentsBuilder builder = UriComponentsBuilder
    //            .fromHttpUrl("https://api.watchmode.com/v1/")
    //            .pathSegment("title", title, "details")
    //            .queryParam("apiKey", apiKey);
    //
    //        var uri = builder.build().toUri();
    //
    //        return restTemplate
    //            .getForEntity(uri, TitleDetail.class)
    //            .getBody()
    //            .getCriticScore();
    //    }
}
