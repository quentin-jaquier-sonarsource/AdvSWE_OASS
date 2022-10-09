package coms.w4156.moviewishlist.Controllers;

import coms.w4156.moviewishlist.Services.WatchModeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StreamingController {

    WatchModeService wms = new WatchModeService();

    @GetMapping("/available")
    public String streaming_services() {

        return wms.getResponse();

    }
}
