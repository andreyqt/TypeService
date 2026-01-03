package holymagic.typeservice.service;

import holymagic.typeservice.dto.RankedRaceDto;
import holymagic.typeservice.mapper.RankedRaceMapper;
import holymagic.typeservice.mapper.RankedRaceMapperImpl;
import holymagic.typeservice.validator.LeaderboardValidator;
import holymagic.typeservice.validator.PublicDataValidator;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LEADERBOARD_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.SPEED_HISTOGRAM_REF;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_GET_LEADERBOARD_URI;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_GET_LEADERBOARD_URI_WITH_ALL_PARAMS;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_NOT_FOUND_EXCEPTION_MSG;
import static holymagic.typeservice.service.LeaderboardServiceTestData.LEADERBOARD_FRIENDS_ONLY_RESPONSE;
import static holymagic.typeservice.service.LeaderboardServiceTestData.LEADERBOARD_NULL_RESPONSE;
import static holymagic.typeservice.service.LeaderboardServiceTestData.LEADERBOARD_RESPONSE;
import static holymagic.typeservice.service.PublicDataServiceTestData.EXPECTED_GET_SPEED_HISTOGRAM_URI;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LeaderboardServiceTest {
    @Mock
    private RestClient restClient;
    @Spy
    private RankedRaceMapperImpl rankedRaceMapper;
    @Spy
    private PublicDataValidator publicDataValidator;
    @Spy
    private LeaderboardValidator leaderboardValidator = new LeaderboardValidator();
    @InjectMocks
    private LeaderboardService leaderboardService;
    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RestClient.ResponseSpec responseSpec;
    private ArgumentCaptor<URI> uriCaptor;

    @BeforeEach
    void setUp() {
        publicDataValidator = new PublicDataValidator();
        ReflectionTestUtils.setField(leaderboardValidator, "maxPageNumber", 1000);
        ReflectionTestUtils.setField(leaderboardValidator, "maxPageSize", 200);
        uriCaptor = ArgumentCaptor.forClass(URI.class);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    public void getLeaderboardTest() {
        when(responseSpec.body(LEADERBOARD_REF)).thenReturn(LEADERBOARD_RESPONSE);
        List<RankedRaceDto> actualDtoList = leaderboardService.getLeaderboard("english", "time",
                "60", null, null, null);

        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(uriCaptor.capture());
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).body(LEADERBOARD_REF);

        List<RankedRaceDto> expectedDtoList = LeaderboardServiceTestData.provideRankedRacesDto();
        assertEquals(expectedDtoList, actualDtoList);
        URI capturedUri = uriCaptor.getValue();
        assertEquals(EXPECTED_GET_LEADERBOARD_URI, capturedUri);
    }

    @Test
    public void getLeaderboardWithNullDataTest() {
        when(responseSpec.body(LEADERBOARD_REF)).thenReturn(LEADERBOARD_NULL_RESPONSE);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            leaderboardService.getLeaderboard("english",
                    "time", "60", null, null, null);
        });
        Assertions.assertEquals(EXPECTED_NOT_FOUND_EXCEPTION_MSG, exception.getMessage());
    }

    @Test
    public void getLeaderboardsForFriendsOnly() {
        when(responseSpec.body(LEADERBOARD_REF)).thenReturn(LEADERBOARD_FRIENDS_ONLY_RESPONSE);
        List<RankedRaceDto> actualDtoList = leaderboardService.getLeaderboard("english", "time",
                "60", 0, 3, true);

        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(uriCaptor.capture());
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).body(LEADERBOARD_REF);

        List<RankedRaceDto> expectedDtoList = LeaderboardServiceTestData.provideRankedRacesDtoForFriendsOnly();
        assertEquals(expectedDtoList, actualDtoList);
        URI capturedUri = uriCaptor.getValue();
        assertEquals(EXPECTED_GET_LEADERBOARD_URI_WITH_ALL_PARAMS, capturedUri);
    }
}
