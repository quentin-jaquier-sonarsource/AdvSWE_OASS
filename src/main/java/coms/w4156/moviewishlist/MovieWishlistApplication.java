package coms.w4156.moviewishlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
@SpringBootApplication
public class MovieWishlistApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieWishlistApplication.class, args);
    }

}
