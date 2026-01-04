package holymagic.typeservice.service;

import holymagic.typeservice.model.Response;
import holymagic.typeservice.model.publicData.TypingStats;
import holymagic.typeservice.validator.PublicDataValidator;
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
import java.util.Map;

import static holymagic.typeservice.model.ParameterizedTypeReferences.SPEED_HISTOGRAM_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.TYPING_STATS_REF;
import static holymagic.typeservice.service.PublicDataServiceTestData.EXPECTED_GET_SPEED_HISTOGRAM_URI;
import static holymagic.typeservice.service.PublicDataServiceTestData.EXPECTED_GET_TYPING_STATS_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublicDataServiceTest {
    @Mock
    private RestClient restClient;
    @Mock
    private PublicDataValidator publicDataValidator;
    @InjectMocks
    private PublicDataService publicDataService;
    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RestClient.ResponseSpec responseSpec;
    private ArgumentCaptor<URI> uriCaptor;

    @BeforeEach
    public void setUp() {
        uriCaptor = ArgumentCaptor.forClass(URI.class);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    public void getSpeedHistogramTest() {
        Response<Map<String, Integer>> response = PublicDataServiceTestData.provideHistogramResponse();
        when(responseSpec.body(SPEED_HISTOGRAM_REF)).thenReturn(response);
        doNothing().when(publicDataValidator).validatePublicDataArgs(anyString(), anyString(), anyString());
        Map<String, Integer> speedHistogram =
                publicDataService.getSpeedHistogram("english", "time", "60");
        assertEquals(response.getData(), speedHistogram);
        verifyRestClientActions(SPEED_HISTOGRAM_REF, EXPECTED_GET_SPEED_HISTOGRAM_URI);
    }

    @Test
    public void getTypingStatsTest() {
        Response<TypingStats> response = PublicDataServiceTestData.provideTypingStatsResponse();
        when(responseSpec.body(TYPING_STATS_REF)).thenReturn(response);
        TypingStats actualStats = publicDataService.getTypingStats();
        assertEquals(response.getData(), actualStats);
        verifyRestClientActions(TYPING_STATS_REF, EXPECTED_GET_TYPING_STATS_URI);
    }

    public void verifyRestClientActions(ParameterizedTypeReference reference, URI expectedUri) {
        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(uriCaptor.capture());
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).body(reference);
        URI capturedUri = uriCaptor.getValue();
        assertEquals(expectedUri, capturedUri);
    }

}
