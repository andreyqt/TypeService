package holymagic.typeservice;

import holymagic.typeservice.service.ExchangeService;
import org.mockito.ArgumentCaptor;
import org.springframework.core.ParameterizedTypeReference;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MyTestUtils {

    public static void verifyExchange(URI expectedUri,
                                      ParameterizedTypeReference reference,
                                      ExchangeService exchangeService,
                                      ArgumentCaptor<URI> uriCaptor) {
        verify(exchangeService, times(1)).makeGetRequest(uriCaptor.capture(), eq(reference));
        assertEquals(expectedUri, uriCaptor.getValue());
    }

}
