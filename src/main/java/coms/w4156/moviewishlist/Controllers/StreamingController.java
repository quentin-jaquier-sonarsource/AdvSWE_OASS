package coms.w4156.moviewishlist.Controllers;

import coms.w4156.moviewishlist.Services.WatchModeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for all endpoints that have to do with accessing the WatchMode API
 */
@RestController
public class StreamingController {

    WatchModeService wms = new WatchModeService("prod");

    @GetMapping("/available")
    public String[] streaming_services() {

        return wms.getResponse();

    }
}
