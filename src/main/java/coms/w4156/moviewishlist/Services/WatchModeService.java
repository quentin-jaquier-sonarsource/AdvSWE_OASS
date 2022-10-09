package coms.w4156.moviewishlist.Services;

import coms.w4156.moviewishlist.utils.Config;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WatchModeService {

    Config config = new Config();
    private final String test_host = "https://yesno.wtf/api";
    private final String watchmode_url = "https://api.watchmode.com/v1/title/1586594/sources/?apiKey="+config.getAPIKey();
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
    public String getResponse() {
        RestTemplate restTemplate = new RestTemplate();


        if (this.mode == "test") {
            YesNo result = restTemplate.getForObject(test_host, YesNo.class);
            return result.getAnswer();
        }
        else {
            ResponseEntity<Source[]> responseEntity = restTemplate.getForEntity(watchmode_url, Source[].class);
            Source[] sources = responseEntity.getBody();

            List<String> sourceNameList = Arrays.stream(sources)
                    .map(Source::getName)
                    .collect(Collectors.toList());

            return "There were "+sources.length+" many sources. The first one is "+sourceNameList.get(0);
        }
    }


}
