package holymagic.typeservice.service;

import holymagic.typeservice.cache.LeaderboardCache;
import holymagic.typeservice.dto.RankedRaceDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.mapper.RankedRaceMapperImpl;
import holymagic.typeservice.mapper.WeeklyActivityMapperImpl;
import holymagic.typeservice.model.leaderboard.Leaderboard;
import holymagic.typeservice.model.leaderboard.RankedRace;
import holymagic.typeservice.model.leaderboard.WeeklyActivity;
import holymagic.typeservice.model.leaderboard.XpLeaderboard;
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

import java.net.URI;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LEADERBOARD_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.RANKED_RACE_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.XP_LEADERBOARD_REF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LeaderboardServiceTest {

    @InjectMocks
    private LeaderboardService leaderboardService;

    @Mock
    private HttpParamValidator httpParamValidator;
    @Spy
    private RankedRaceMapperImpl rankedRaceMapper;
    @Spy
    private WeeklyActivityMapperImpl weeklyActivityMapper;
    @Mock
    private LeaderboardCache leaderboardCache;
    @Mock
    private ExchangeService exchangeService;

    ArgumentCaptor<URI> uriCaptor;
    List<RankedRace> rankedRaces;
    List<RankedRaceDto> rankedRaceDtos;
    Leaderboard leaderboard;

    @BeforeEach
    void setUp() {
        uriCaptor = ArgumentCaptor.forClass(URI.class);
        rankedRaces = ServiceTestData.provideRankedRaces();
        rankedRaceDtos = ServiceTestData.provideRankedRaceDtos();
        leaderboard = new Leaderboard(rankedRaces.size(), rankedRaces);
    }

    @Test
    public void getLeaderboardTest() {
        URI expectedUri = URI.create("/leaderboards?language=english&mode=time&mode2=60");
        when(exchangeService.makeGetRequest(any(URI.class), eq(LEADERBOARD_REF))).thenReturn(leaderboard);

        List<RankedRaceDto> actualDtos = leaderboardService.getLeaderboard("english", "time", "60",
                null, null, null);
        assertEquals(rankedRaceDtos, actualDtos);

        verifyExchange(expectedUri, LEADERBOARD_REF);
        verify(leaderboardCache, times(0)).getAll();
        verify(rankedRaceMapper, times(1)).toDto(rankedRaces);
    }

    @Test
    public void getRankTest() {
        URI expectedUri = URI.create("/leaderboards/rank?language=english&mode=time&mode2=60");
        when(exchangeService.makeGetRequest(any(URI.class), eq(RANKED_RACE_REF))).thenReturn(rankedRaces.get(0));

        RankedRaceDto actualDto = leaderboardService.getRank("english", "time", "60",
                null);
        assertEquals(rankedRaceDtos.get(0), actualDto);

        verifyExchange(expectedUri, RANKED_RACE_REF);
        verify(rankedRaceMapper, times(1)).toDto(rankedRaces.get(0));
    }

    @Test
    public void getDailyLeaderboardTest() {
        URI expectedUri = URI.create("/leaderboards/daily?language=english&mode=time&mode2=60");
        when(exchangeService.makeGetRequest(any(URI.class), eq(LEADERBOARD_REF))).thenReturn(leaderboard);

        List<RankedRaceDto> actualDtos = leaderboardService.getDailyLeaderboard("english", "time",
                "60", null, null, null);
        assertEquals(rankedRaceDtos, actualDtos);

        verifyExchange(expectedUri, LEADERBOARD_REF);
        verify(rankedRaceMapper, times(1)).toDto(rankedRaces);
    }

    @Test
    public void getWeeklyXpActivityTest() {
        URI expectedUri =  URI.create("/leaderboards/xp/weekly");
        List<WeeklyActivity> activity = ServiceTestData.provideWeeklyActivity();
        List<WeeklyActivityDto> expectedActivity = ServiceTestData.provideWeeklyActivityDtos();
        XpLeaderboard xpLeaderboard = new XpLeaderboard(activity.size(), activity);
        when(exchangeService.makeGetRequest(any(URI.class), eq(XP_LEADERBOARD_REF))).thenReturn(xpLeaderboard);

        List<WeeklyActivityDto> actualActivity = leaderboardService.getWeeklyXpLeaderboard(null, null,
                null);

        assertEquals(expectedActivity, actualActivity);
        verifyExchange(expectedUri, XP_LEADERBOARD_REF);
        verify(weeklyActivityMapper, times(1)).toDto(activity);
    }

    public void verifyExchange(URI expectedUri, ParameterizedTypeReference reference) {
        verify(exchangeService, times(1)).makeGetRequest(uriCaptor.capture(), eq(reference));
        assertEquals(expectedUri, uriCaptor.getValue());
    }

}
