package coms.w4156.moviewishlist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public final class MovieWishlistApplication {

    /**
     * Private constructor for Utility Class.
     */
    private MovieWishlistApplication() { }

    /**
     * Runs the service.
     * @param args Command line arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(MovieWishlistApplication.class, args);
    }
}
