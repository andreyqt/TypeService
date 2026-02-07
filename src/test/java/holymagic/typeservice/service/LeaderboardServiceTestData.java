package holymagic.typeservice.service;

import holymagic.typeservice.dto.RankingDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.mapper.RankingMapper;
import holymagic.typeservice.mapper.RankingMapperImpl;
import holymagic.typeservice.mapper.WeeklyActivityMapper;
import holymagic.typeservice.mapper.WeeklyActivityMapperImpl;
import holymagic.typeservice.model.leaderboard.Ranking;
import holymagic.typeservice.model.leaderboard.WeeklyActivity;
import holymagic.typeservice.model.leaderboard.XpLeaderboard;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class LeaderboardServiceTestData {

    public static XpLeaderboard provideXpLeaderboard() {
        return new XpLeaderboard(5, provideWeeklyActivity());
    }

    public static List<Ranking> provideRankings() {
        List<Ranking> rankedRaces = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int rank = i + 1;
            double wpm = 350. - i * 1.0;
            Ranking ranking = Ranking.builder()
                    .rank(rank)
                    .uid("test_id_" + rank)
                    .acc(100. - rank)
                    .name("test_name_" + rank)
                    .timestamp(1767214800000L + rank * 1000)
                    .wpm(wpm)
                    .build();
            rankedRaces.add(ranking);
        }
        return rankedRaces;
    }

    public static List<RankingDto> provideRankingDtos() {
        RankingMapper mapper = new RankingMapperImpl();
        return mapper.toDto(provideRankings());
    }

    public static List<WeeklyActivity> provideWeeklyActivity() {
        List<WeeklyActivity> rankings = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int rank = i + 1;
            WeeklyActivity activity = WeeklyActivity.builder()
                    .rank(rank)
                    .uid("test_id_" + rank)
                    .totalXp(500 * rank)
                    .name("test_name_" + rank)
                    .lastActivityTimestamp(1767214800000L + rank * 1000)
                    .build();
            rankings.add(activity);
        }
        return rankings;
    }

    public static List<WeeklyActivityDto> provideWeeklyActivityDtos() {
        WeeklyActivityMapper mapper = new WeeklyActivityMapperImpl();
        return mapper.toDto(provideWeeklyActivity());
    }

    public static final URI EXPECTED_GET_LBS_URI =
            URI.create("/leaderboards?language=english&mode=time&pageSize=5&mode2=60&friendsOnly=false");
    public static final URI EXPECTED_GET_RANK_URI =
            URI.create("/leaderboards/rank?language=english&mode=time&mode2=60&friendsOnly=false");
    public static final URI EXPECTED_GET_DAILY_LBS_URI =
            URI.create("/leaderboards/daily?language=english&mode=time&pageSize=5&mode2=60&friendsOnly=false");
    public static final URI EXPECTED_GET_WEEKLY_XP_URI =
            URI.create("/leaderboards/xp/weekly?friendsOnly=false");

}
