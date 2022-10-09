package coms.w4156.moviewishlist.Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class DummyController {

    @GetMapping("/test")
    String test() {
        return "All good!";
    }

    @GetMapping("/rand")
    String rand() {
        Random r = new Random();

        return Double.toString(r.nextDouble());
    }


}
