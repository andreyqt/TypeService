package holymagic.typeservice.service;

import holymagic.typeservice.exception.DataValidationException;
import holymagic.typeservice.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExchangeServiceTest {

    @InjectMocks
    private ExchangeService exchangeService;

    @Mock
    private RestClient restClient;
    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RestClient.ResponseSpec responseSpec;

    private URI testUri;
    private ArgumentCaptor<URI> uriCaptor;
    private Response<String> testResponse;
    private Response<String> testNullResponse;
    private ParameterizedTypeReference<Response<String>> testReference;


    @BeforeEach
    public void setUp() {
        uriCaptor = ArgumentCaptor.forClass(URI.class);
        testUri = URI.create("https://example.com");
        testReference = new ParameterizedTypeReference<Response<String>>() {};
        testResponse = new Response<>("test message", "test data");
        testNullResponse = new Response<>("test message", null);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(uriCaptor.capture())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

    }

    @Test
    public void makeGetRequestTest() {
        when(responseSpec.body(testReference)).thenReturn(testResponse);
        String actualResponse = exchangeService.makeGetRequest(testUri, testReference);
        assertEquals("test data", actualResponse);
        verifyRestClientActions(testUri, testReference);
    }

    @Test
    public void makeGetRequestAndReceiveNullData() {
        when(responseSpec.body(testReference)).thenReturn(testNullResponse);
        Exception exception = assertThrows(DataValidationException.class,
                () -> exchangeService.makeGetRequest(testUri, testReference));
        assertEquals("Response data is null", exception.getMessage());
    }

    public void verifyRestClientActions(URI expectedUri, ParameterizedTypeReference reference) {
        verify(restClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(uriCaptor.capture());
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).body(reference);
        URI capturedUri = uriCaptor.getValue();
        assertEquals(expectedUri, capturedUri);
    }
}
