package coms.w4156.moviewishlist.Controllers;

import coms.w4156.moviewishlist.Services.WatchModeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for all endpoints that have to do with accessing the WatchMode API.
 */
@RestController
public class StreamingController {

    WatchModeService wms = new WatchModeService("prod");

    @GetMapping("/available")
    public String[] streamingServices() {

        return wms.testResponse();

    }

    @GetMapping("/freeWithSub")
    @ResponseBody
    public String[] getSourcesFreeWithSubscription(final @RequestParam String id) {
        return wms.getFreeWithSubSourcesById(id);
    }
}
