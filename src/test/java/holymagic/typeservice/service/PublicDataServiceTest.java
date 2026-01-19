package holymagic.typeservice.service;

import holymagic.typeservice.model.publicData.TypingStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;

import java.net.URI;
import java.util.Map;

import static holymagic.typeservice.model.ParameterizedTypeReferences.SPEED_HISTOGRAM_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.TYPING_STATS_REF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PublicDataServiceTest {

    @InjectMocks
    private PublicDataService publicDataService;
    @Mock
    private ExchangeService exchangeService;

    ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);

    @Test
    public void getSpeedHistogramTest() {
        Map<String, Integer> speedHistogram = PublicDataServiceTestData.provideHistogram();
        URI expectedUri = URI.create("/public/speedHistogram?language=english&mode=time&mode2=60");
        when(exchangeService.makeGetRequest(any(URI.class), eq(SPEED_HISTOGRAM_REF))).thenReturn(speedHistogram);
        Map<String, Integer> actualHistogram = publicDataService.getSpeedHistogram("english", "time", "60");
        assertEquals(speedHistogram, actualHistogram);
        verifyExchange(expectedUri, SPEED_HISTOGRAM_REF);
    }

    @Test
    public void getTypingStatsTest() {
        TypingStats expectedStats = PublicDataServiceTestData.provideTypingStats();
        URI expectedUri = URI.create("/public/typingStats");
        when(exchangeService.makeGetRequest(any(URI.class), eq(TYPING_STATS_REF))).thenReturn(expectedStats);
        TypingStats actualStats = publicDataService.getTypingStats();
        assertEquals(expectedStats, actualStats);
        verifyExchange(expectedUri, TYPING_STATS_REF);
    }

    public void verifyExchange(URI expectedUri, ParameterizedTypeReference reference) {
        verify(exchangeService, times(1)).makeGetRequest(uriCaptor.capture(), eq(reference));
        assertEquals(expectedUri, uriCaptor.getValue());
    }

}
