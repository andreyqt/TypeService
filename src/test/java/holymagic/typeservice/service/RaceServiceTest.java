package holymagic.typeservice.service;

import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.mapper.RaceMapper;
import holymagic.typeservice.mapper.RaceMapperImpl;
import holymagic.typeservice.validator.RaceValidator;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LIST_OF_RACES_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.RACE_REF;
import static holymagic.typeservice.service.RaceServiceTestData.EXPECTED_GET_BY_ID_RESULT_URI;
import static holymagic.typeservice.service.RaceServiceTestData.EXPECTED_GET_LAST_RESULT_URI;
import static holymagic.typeservice.service.RaceServiceTestData.EXPECTED_GET_RESULTS_URI;
import static holymagic.typeservice.service.RaceServiceTestData.EXPECTED_GET_RESULTS_WITH_PARAMS_URI;
import static holymagic.typeservice.service.RaceServiceTestData.EXPECTED_LIST_OF_DTO_RACES;
import static holymagic.typeservice.service.RaceServiceTestData.EXPECTED_RACE_DTO;
import static holymagic.typeservice.service.RaceServiceTestData.RESPONSE_OF_RACES;
import static holymagic.typeservice.service.RaceServiceTestData.RESPONSE_WITH_NULL;
import static holymagic.typeservice.service.RaceServiceTestData.RESPONSE_WITH_SINGLE_RESULT;
import static holymagic.typeservice.service.RaceServiceTestData.SINGLE_RESPONSE_WITH_NULL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RaceServiceTest {
    @InjectMocks
    private RaceService raceService;
    @Mock
    private RaceValidator raceValidator;
    @Mock
    private RestClient restClient;
    @Spy
    private RaceMapper raceMapper = new RaceMapperImpl();
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
    @DisplayName("when every arg is null")
    public void getResultsWithNoParametersTest() {
        when(responseSpec.body(LIST_OF_RACES_REF)).thenReturn(RESPONSE_OF_RACES);
        List<RaceDto> actualListOfDtoRaces = raceService.getResults(null,null,null);

        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(uriCaptor.capture());
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).body(LIST_OF_RACES_REF);

        assertEquals(EXPECTED_LIST_OF_DTO_RACES, actualListOfDtoRaces);
        URI capturedUri = uriCaptor.getValue();
        assertEquals(EXPECTED_GET_RESULTS_URI, capturedUri);
    }

    @Test
    @DisplayName("receiving response with null data")
    public void getResultsWithNullDataTest() {
        when(responseSpec.body(LIST_OF_RACES_REF)).thenReturn(RESPONSE_WITH_NULL);
        assertThrows(NotFoundException.class,
                () -> raceService.getResults(null,null,null));
    }

    @Test
    @DisplayName("when every arg is valid")
    public void getResultsWithValidParametersTest() {
        when(responseSpec.body(LIST_OF_RACES_REF)).thenReturn(RESPONSE_OF_RACES);
        doNothing().when(raceValidator).validateLimit(any(Integer.class));
        doNothing().when(raceValidator).validateOffset(any(Integer.class));
        doNothing().when(raceValidator).validateTimestamp(any(Long.class));

        List<RaceDto> actualListOfDtoRaces = raceService.getResults(1589428800000L,0,3);
        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(uriCaptor.capture());
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).body(LIST_OF_RACES_REF);
        verify(raceValidator).validateLimit(3);
        verify(raceValidator).validateTimestamp(1589428800000L);
        verify(raceValidator).validateOffset(0);

        assertEquals(EXPECTED_LIST_OF_DTO_RACES, actualListOfDtoRaces);
        URI capturedUri = uriCaptor.getValue();
        UriComponents actualUri = UriComponentsBuilder.fromUri(capturedUri).build();
        assertEquals(EXPECTED_GET_RESULTS_WITH_PARAMS_URI, capturedUri);
        assertEquals("1589428800000", actualUri.getQueryParams().getFirst("onOrAfterTimestamp"));
        assertEquals("0", actualUri.getQueryParams().getFirst("offset"));
        assertEquals("3", actualUri.getQueryParams().getFirst("limit"));
    }

    @Test
    public void getResultByIdTest() {
        when(responseSpec.body(RACE_REF)).thenReturn(RESPONSE_WITH_SINGLE_RESULT);
        RaceDto actualRaceDto = raceService.getResultById("fi35d345");

        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(uriCaptor.capture());
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).body(RACE_REF);
        assertEquals(EXPECTED_RACE_DTO, actualRaceDto);

        URI capturedUri = uriCaptor.getValue();
        assertEquals(EXPECTED_GET_BY_ID_RESULT_URI, capturedUri);
    }

    @Test
    public void getResultByInvalidId() {
        when(responseSpec.body(RACE_REF)).thenReturn(SINGLE_RESPONSE_WITH_NULL);
        assertThrows(NotFoundException.class, () -> raceService.getResultById("fi35d345"));
    }

    @Test
    public void getLastResultTest() {
        when(responseSpec.body(RACE_REF)).thenReturn(RESPONSE_WITH_SINGLE_RESULT);
        RaceDto actualRaceDto = raceService.getLastResult();

        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(uriCaptor.capture());
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).body(RACE_REF);
        assertEquals(EXPECTED_RACE_DTO, actualRaceDto);

        URI capturedUri = uriCaptor.getValue();
        assertEquals(EXPECTED_GET_LAST_RESULT_URI, capturedUri);
    }

    @Test
    public void getLastResultWithExceptionTest() {
        when(responseSpec.body(RACE_REF)).thenReturn(SINGLE_RESPONSE_WITH_NULL);
        assertThrows(NotFoundException.class, () -> raceService.getLastResult());
    }
}
