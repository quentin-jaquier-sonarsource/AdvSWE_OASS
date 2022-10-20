package coms.w4156.moviewishlist.controllers;

import coms.w4156.moviewishlist.services.WatchModeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for all endpoints that have to do with accessing
 * the WatchMode API.
 */
@RestController
public class StreamingController {

    /**
     * Instance of the WatchModeService used to execute logic related to
     * querying the WatchMode API.
     */
    private WatchModeService wms = new WatchModeService();

    /**
     * A test endpoint that returns all streaming services a fixed movie is
     * available on. So long as the service is free with a subscription (i.e.
     * does not return services where you'd have to buy or rent the movie)
     * @return A list of streaming services.
     */
    @GetMapping("/available")
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
    @GetMapping("/freeWithSub")
    @ResponseBody
    public List<String> getSourcesFreeWithSub(final @RequestParam String id) {
        return wms.getFreeWithSubSourcesById(id);
    }
}
