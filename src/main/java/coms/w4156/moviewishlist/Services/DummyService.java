package coms.w4156.moviewishlist.Services;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public class DummyService {
    /**
     * Dummy method that simply returns a number.
     * @return a fixed number
     */
    public String getNum() {
        return "5";
    }
}
