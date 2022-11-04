package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.services.WatchModeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static coms.w4156.moviewishlist.utils.StreamingConstants.STREAMING_BUY_ENDPOINT;
import static coms.w4156.moviewishlist.utils.StreamingConstants.STREAMING_RENT_ENDPOINT;
import static coms.w4156.moviewishlist.utils.StreamingConstants.STREAMING_SUB_ENDPOINT;
import static coms.w4156.moviewishlist.utils.StreamingConstants.STREAMING_TEST_ENDPOINT;

/**
 * Controller for all endpoints that have to do with accessing
 * the WatchMode API.
 */
@RequestMapping(value = "/streaming")
@RestController
public class StreamingController {

    /**
     * Instance of the WatchModeService used to execute logic related to
     * querying the WatchMode API.
     */
    @Autowired
    private WatchModeService wms = new WatchModeService();

    /**
     * A test endpoint that returns all streaming services a fixed movie is
     * available on. So long as the service is free with a subscription (i.e.
     * does not return services where you'd have to buy or rent the movie)
     * @return A list of streaming services.
     */
    @GetMapping(STREAMING_TEST_ENDPOINT)
    public List<String> streamingServices() {
        return wms.testResponse();
    }

    /**
     * Endpoint for getting all sources where a movie is available on for free
     * with a subscription. Takes in a movie's WatchMode ID in order to query
     * the WatchMode API.
     * @param id The WatchMode ID for the movie we are interested in.
     * @return A list of all the streaming services this movie is available
     * on for free with subscription.
     */
    @GetMapping(STREAMING_SUB_ENDPOINT + "/{id}")
    @ResponseBody
    public List<String> getSourcesFreeWithSub(final @PathVariable String id) {

        return wms.getFreeWithSubSourcesById(id);
    }

    /**
     * Endpoint for getting all sources where a movie is available on to rent.
     * Takes in a movie's WatchMode ID in order to query the WatchMode API.
     * @param id The WatchMode ID for the movie we are interested in.
     * @return A list of all the streaming services this movie is available
     * on to rent.
     */
    @GetMapping(STREAMING_RENT_ENDPOINT + "/{id}")
    @ResponseBody
    public List<String> getSourcesRent(final @PathVariable String id) {

        return wms.getRentSourcesById(id);
    }

    /**
     * Endpoint for getting all sources where a movie is available on to buy.
     * Takes in a movie's WatchMode ID in order to query the WatchMode API.
     * @param id The WatchMode ID for the movie we are interested in.
     * @return A list of all the streaming services this movie is available
     * on to buy.
     */
    @GetMapping(STREAMING_BUY_ENDPOINT + "/{id}")
    @ResponseBody
    public List<String> getSourcesBuy(final @PathVariable String id) {

        return wms.getBuySourcesById(id);
    }
}
