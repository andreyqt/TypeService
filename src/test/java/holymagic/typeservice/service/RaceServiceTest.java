package holymagic.typeservice.service;

import holymagic.typeservice.cache.RaceCache;
import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.mapper.RaceMapper;
import holymagic.typeservice.mapper.RaceMapperImpl;
import holymagic.typeservice.model.race.Race;
import holymagic.typeservice.repository.RaceRepository;
import holymagic.typeservice.validator.HttpParamValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LIST_OF_RACES_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.RACE_REF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RaceServiceTest {

    @Mock
    private RaceRepository raceRepository;
    @Mock
    private HttpParamValidator httpParamValidator;
    @Mock
    private ExchangeService exchangeService;
    @Mock
    private RaceCache raceCache;
    @Spy
    private RaceMapper raceMapper = new RaceMapperImpl();

    @InjectMocks
    private RaceService raceService;

    private List<Race> races;
    private List<RaceDto> raceDtos;
    private ArgumentCaptor<URI> uriCaptor;

    @BeforeEach
    void setUp() {
        int defaultLimit = 10;
        races = ServiceTestData.provideRaces();
        raceDtos = ServiceTestData.provideRaceDtos();
        uriCaptor = ArgumentCaptor.forClass(URI.class);
        ReflectionTestUtils.setField(raceService, "defaultLimit", defaultLimit);
    }

    @Test
    public void getResultsByRequestTest() {
        URI expectedUri = URI.create("/results?limit=10");
        when(exchangeService.makeGetRequest(any(URI.class), eq(LIST_OF_RACES_REF))).thenReturn(races);
        List<RaceDto> actualRaceDtos = raceService.getResultsFromRequest(null, null, null);
        assertEquals(raceDtos, actualRaceDtos);
        verifyExchange(expectedUri, LIST_OF_RACES_REF);
        verify(raceMapper, times(1)).toDto(races);
    }

    @Test
    public void getResultsByRequestTestWithParams() {
        URI uri = URI.create("/results?onOrAfterTimestamp=1589428800000&offset=0&limit=10");
        when(exchangeService.makeGetRequest(any(URI.class), eq(LIST_OF_RACES_REF))).thenReturn(races);
        List<RaceDto> actualRaceDtos = raceService.getResultsFromRequest(1589428800000L, 0, 10);
        assertEquals(raceDtos, actualRaceDtos);
        verifyExchange(uri, LIST_OF_RACES_REF);
        verifyValidation();
        verify(raceMapper, times(1)).toDto(races);
    }

    @Test
    public void getByIdTest() {
        String id = "fi35d345";
        URI expectedUri = URI.create("/results/id/" + id);
        when(exchangeService.makeGetRequest(any(URI.class), eq(RACE_REF))).thenReturn(races.getFirst());
        RaceDto actualRaceDto = raceService.getResultByIdFromRequest(id);
        assertEquals(raceDtos.getFirst(), actualRaceDto);
        verifyExchange(expectedUri, RACE_REF);
        verify(raceMapper, times(1)).toDto(races.getFirst());
    }

    @Test
    public void getLastTest() {
        URI expectedUri = URI.create("/results/last");
        when(exchangeService.makeGetRequest(any(URI.class), eq(RACE_REF))).thenReturn(races.getLast());
        RaceDto actualRaceDto = raceService.getLastResultFromRequest();
        assertEquals(raceDtos.getLast(), actualRaceDto);
        verifyExchange(expectedUri, RACE_REF);
        verify(raceMapper, times(1)).toDto(races.getLast());
    }

    public void verifyExchange(URI expectedUri, ParameterizedTypeReference reference) {
        verify(exchangeService, times(1)).makeGetRequest(uriCaptor.capture(), eq(reference));
        assertEquals(expectedUri, uriCaptor.getValue());
    }

    public void verifyValidation() {
        verify(httpParamValidator, times(1)).validateTimestamp(anyLong());
        verify(httpParamValidator, times(1)).validateLimit(anyInt());
        verify(httpParamValidator, times(1)).validateOffset(anyInt());
    }

}
