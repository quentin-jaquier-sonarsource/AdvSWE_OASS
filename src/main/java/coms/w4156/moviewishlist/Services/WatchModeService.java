package coms.w4156.moviewishlist.Services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpResponse;
@Service
public class WatchModeService {
    private final String host = "https://yesno.wtf/api";
    private final String charset = "UTF-8";

    public WatchModeService() {

    }

    /**
     * @return response from the api
     */
    public String getResponse() {
        RestTemplate restTemplate = new RestTemplate();

        YesNo result = restTemplate.getForObject(host, YesNo.class);

        return result.getAnswer();
    }


}
