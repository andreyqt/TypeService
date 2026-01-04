package holymagic.typeservice.service;

import holymagic.typeservice.dto.RankedRaceDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.mapper.RankedRaceMapperImpl;
import holymagic.typeservice.mapper.WeeklyActivityMapperImpl;
import holymagic.typeservice.validator.LeaderboardValidator;
import holymagic.typeservice.validator.PublicDataValidator;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LEADERBOARD_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.RANKED_RACE_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.XP_LEADERBOARD_REF;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_GET_DAILY_LEADERBOARD_URI;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_GET_LEADERBOARD_URI;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_GET_LEADERBOARD_URI_WITH_ALL_PARAMS;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_GET_RANK_URI;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_GET_XP_LEADERBOARD_URI;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_NOT_FOUND_EXCEPTION_MSG;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_NOT_FOUND_EXCEPTION_RANK_MSG;
import static holymagic.typeservice.service.LeaderboardServiceTestData.LEADERBOARD_FRIENDS_ONLY_RESPONSE;
import static holymagic.typeservice.service.LeaderboardServiceTestData.LEADERBOARD_NULL_RESPONSE;
import static holymagic.typeservice.service.LeaderboardServiceTestData.LEADERBOARD_RESPONSE;
import static holymagic.typeservice.service.LeaderboardServiceTestData.RANKED_RACE_NULL_RESPONSE;
import static holymagic.typeservice.service.LeaderboardServiceTestData.RANKED_RACE_RESPONSE;
import static holymagic.typeservice.service.LeaderboardServiceTestData.XP_LEADERBOARD_RESPONSE;
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
    private WeeklyActivityMapperImpl weeklyActivityMapper;
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
        verifyRestClientActions(LEADERBOARD_REF, EXPECTED_GET_LEADERBOARD_URI);
        List<RankedRaceDto> expectedDtoList = LeaderboardServiceTestData.provideRankedRacesDto();
        assertEquals(expectedDtoList, actualDtoList);
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
        verifyRestClientActions(LEADERBOARD_REF, EXPECTED_GET_LEADERBOARD_URI_WITH_ALL_PARAMS);
        List<RankedRaceDto> expectedDtoList = LeaderboardServiceTestData.provideRankedRacesDtoForFriendsOnly();
        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    public void getRankTest() {
        when(responseSpec.body(RANKED_RACE_REF)).thenReturn(RANKED_RACE_RESPONSE);
        RankedRaceDto actualRankedDto = leaderboardService.getRank("english", "time", "60",
                null);
        verifyRestClientActions(RANKED_RACE_REF, EXPECTED_GET_RANK_URI);
        RankedRaceDto expectedRankedDto = LeaderboardServiceTestData.provideRankedRaceDto();
        assertEquals(expectedRankedDto, actualRankedDto);
    }

    @Test
    public void getRankWithNullDataTest() {
        when(responseSpec.body(RANKED_RACE_REF)).thenReturn(RANKED_RACE_NULL_RESPONSE);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            leaderboardService.getRank("english",
                    "time", "60", null);
        });
        Assertions.assertEquals(EXPECTED_NOT_FOUND_EXCEPTION_RANK_MSG, exception.getMessage());
    }

    @Test
    public void getDailyLeaderboardTest() {
        when(responseSpec.body(LEADERBOARD_REF)).thenReturn(LEADERBOARD_RESPONSE);
        List<RankedRaceDto> actualDtoList = leaderboardService.getDailyLeaderboard("english", "time",
                "60", null, null, null);
        verifyRestClientActions(LEADERBOARD_REF, EXPECTED_GET_DAILY_LEADERBOARD_URI);
        List<RankedRaceDto> expectedDtoList = LeaderboardServiceTestData.provideRankedRacesDto();
        assertEquals(expectedDtoList, actualDtoList);
    }

    @Test
    public void getWeeklyXpLeaderboardTest() {
        when(responseSpec.body(XP_LEADERBOARD_REF)).thenReturn(XP_LEADERBOARD_RESPONSE);
        List<WeeklyActivityDto> actualDtoList = leaderboardService.getWeeklyXpLeaderboard(null,
                null, null);
        verifyRestClientActions(XP_LEADERBOARD_REF, EXPECTED_GET_XP_LEADERBOARD_URI);
        List<WeeklyActivityDto> expectedDtoList = LeaderboardServiceTestData.provideWeeklyActivitiesDto();
        assertEquals(expectedDtoList, actualDtoList);
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
