package coms.w4156.moviewishlist.Services;

import coms.w4156.moviewishlist.utils.Config;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class to abstract away the logic related to getting information from the WatchMode API
 */
@Service
public class WatchModeService {

    Config config = new Config();
    private final String test_host = "https://yesno.wtf/api";
    private final String parasiteId = "1295258";
    private final String elCaminoId = "1586594 ";
    private final String watchmode_url = "https://api.watchmode.com/v1/title/"+parasiteId+"/sources/?apiKey="+config.getAPIKey();
    private final String charset = "UTF-8";
    String mode;

    public WatchModeService() {
        this.mode = "test";
    }

    public WatchModeService(String mode) {
        this.mode = mode;
    }

    /**
     * @return response from the api
     */
    public String[] getResponse() {
        RestTemplate restTemplate = new RestTemplate();


        if (this.mode == "test") {
            YesNo result = restTemplate.getForObject(test_host, YesNo.class);
            return new String[] {result.getAnswer()};
        }
        else {
            ResponseEntity<Source[]> responseEntity = restTemplate.getForEntity(watchmode_url, Source[].class);
            Source[] sources = responseEntity.getBody();

            List<String> sourceNameList = Arrays.stream(sources)
                    .filter(Source::isFreeWithSubscription)
                    .map(Source::getName)
                    .collect(Collectors.toList());

            return sourceNameList.toArray(new String[0]);
        }
    }


}
