package holymagic.typeservice.service;

import holymagic.typeservice.MyTestUtils;
import holymagic.typeservice.dto.RankingDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.mapper.RankingMapperImpl;
import holymagic.typeservice.mapper.WeeklyActivityMapperImpl;
import holymagic.typeservice.model.leaderboard.Leaderboard;
import holymagic.typeservice.model.leaderboard.Ranking;
import holymagic.typeservice.model.leaderboard.WeeklyActivity;
import holymagic.typeservice.model.leaderboard.XpLeaderboard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.net.URI;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LEADERBOARD_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.RANKING_RACE_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.XP_LEADERBOARD_REF;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_GET_DAILY_LBS_URI;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_GET_LBS_URI;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_GET_RANK_URI;
import static holymagic.typeservice.service.LeaderboardServiceTestData.EXPECTED_GET_WEEKLY_XP_URI;
import static holymagic.typeservice.service.LeaderboardServiceTestData.provideXpLeaderboard;
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

    @Spy
    private RankingMapperImpl rankingMapper;
    @Spy
    private WeeklyActivityMapperImpl weeklyActivityMapper;
    @Mock
    private ExchangeService exchangeService;

    ArgumentCaptor<URI> uriCaptor;
    List<Ranking> rankings;
    List<RankingDto> rankingDtos;
    Leaderboard leaderboard;

    @BeforeEach
    void setUp() {
        uriCaptor = ArgumentCaptor.forClass(URI.class);
        rankings = LeaderboardServiceTestData.provideRankings();
        rankingDtos = LeaderboardServiceTestData.provideRankingDtos();
        leaderboard = new Leaderboard(rankings.size(), rankings);
        ReflectionTestUtils.setField(leaderboardService, "pageSize", 5);
    }

    @Test
    public void getLeaderboardTest() {
        when(exchangeService.makeGetRequest(any(URI.class), eq(LEADERBOARD_REF))).thenReturn(leaderboard);
        List<RankingDto> actualDtos = leaderboardService.getLeaderboard("english", "time",
                "60", false);
        assertEquals(rankingDtos, actualDtos);
        MyTestUtils.verifyExchange(EXPECTED_GET_LBS_URI, LEADERBOARD_REF, exchangeService, uriCaptor);
        verify(rankingMapper, times(1)).toDto(rankings);
    }

    @Test
    public void getRankTest() {
        when(exchangeService.makeGetRequest(any(URI.class), eq(RANKING_RACE_REF))).thenReturn(rankings.getFirst());
        RankingDto actualDto = leaderboardService.getRank("english", "time", "60", false);
        assertEquals(rankingDtos.getFirst(), actualDto);
        MyTestUtils.verifyExchange(EXPECTED_GET_RANK_URI, RANKING_RACE_REF, exchangeService, uriCaptor);
        verify(rankingMapper, times(1)).toDto(rankings.getFirst());
    }

    @Test
    public void getDailyLeaderboardTest() {
        when(exchangeService.makeGetRequest(any(URI.class), eq(LEADERBOARD_REF))).thenReturn(leaderboard);
        List<RankingDto> actualDtos = leaderboardService.getDailyLeaderboard("english", "time",
                "60", false);
        assertEquals(rankingDtos, actualDtos);
        MyTestUtils.verifyExchange(EXPECTED_GET_DAILY_LBS_URI, LEADERBOARD_REF, exchangeService, uriCaptor);
        verify(rankingMapper, times(1)).toDto(rankings);
    }

    @Test
    public void getWeeklyXpActivityTest() {
        List<WeeklyActivityDto> expectedActivity = LeaderboardServiceTestData.provideWeeklyActivityDtos();
        XpLeaderboard xpLeaderboard = LeaderboardServiceTestData.provideXpLeaderboard();
        when(exchangeService.makeGetRequest(any(URI.class), eq(XP_LEADERBOARD_REF))).thenReturn(xpLeaderboard);

        List<WeeklyActivityDto> actualActivity = leaderboardService.getWeeklyXpLeaderboard(false);

        assertEquals(expectedActivity, actualActivity);
        MyTestUtils.verifyExchange(EXPECTED_GET_WEEKLY_XP_URI, XP_LEADERBOARD_REF, exchangeService, uriCaptor);
        verify(weeklyActivityMapper, times(5)).toDto(any(WeeklyActivity.class));
    }

}
