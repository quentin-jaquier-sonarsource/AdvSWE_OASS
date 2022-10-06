package coms.w4156.moviewishlist.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @GetMapping("/test")
//    @ResponseBody
    String test() {
        return "All good!";
    }
}
