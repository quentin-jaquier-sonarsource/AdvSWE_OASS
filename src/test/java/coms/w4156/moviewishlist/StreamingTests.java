package coms.w4156.moviewishlist;

import coms.w4156.moviewishlist.Services.Source;
import coms.w4156.moviewishlist.Services.WatchModeService;
import coms.w4156.moviewishlist.utils.Config;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class StreamingTests {

    Config config = new Config();
    private final String skyfallId = "1350564";
    private final String watchmode_url = "https://api.watchmode.com/v1/title/"+skyfallId+"/sources/?apiKey="+config.getApikey();


    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WatchModeService wms = new WatchModeService("prod");

    @Test
    public void testAvailable() {
        Source netflix = new Source();
        netflix.setName("Netflix");
        netflix.setType("sub");

        Source amazon = new Source();
        amazon.setName("Amazon");
        amazon.setType("sub");

        Source vudu = new Source();
        vudu.setName("VUDU");
        vudu.setType("buy");

        Source[] sourcesAvailableFreeWithSubscription = new Source[]{netflix, amazon};
        Source[] allSources = new Source[]{netflix, amazon, vudu};

        Mockito
                .when(restTemplate.getForEntity(watchmode_url, Source[].class))
                .thenReturn(new ResponseEntity(allSources, HttpStatus.OK));

        String[] returnedSources = wms.testResponse();

        Assertions.assertEquals(2, returnedSources.length);
    }

}
