package holymagic.typeservice.service;

import holymagic.typeservice.dto.RankedRaceDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.mapper.RankedRaceMapper;
import holymagic.typeservice.mapper.RankedRaceMapperImpl;
import holymagic.typeservice.mapper.WeeklyActivityMapper;
import holymagic.typeservice.mapper.WeeklyActivityMapperImpl;
import holymagic.typeservice.model.leaderboard.RankedRace;
import holymagic.typeservice.model.leaderboard.WeeklyActivity;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardServiceTestData {

    public static List<RankedRace> provideRankedRaces() {
        List<RankedRace> rankedRaces = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int rank = i + 1;
            double wpm = 350. - i * 1.0;
            RankedRace rankedRace = RankedRace.builder()
                    .rank(rank)
                    .uid("test_id_" + rank)
                    .acc(100. - rank)
                    .name("test_name_" + rank)
                    .timestamp(1767214800000L + rank * 1000)
                    .wpm(wpm)
                    .build();
            rankedRaces.add(rankedRace);
        }
        return rankedRaces;
    }

    public static List<RankedRaceDto> provideRankedRaceDtos() {
        RankedRaceMapper mapper = new RankedRaceMapperImpl();
        return mapper.toDto(provideRankedRaces());
    }

    public static List<WeeklyActivity> provideWeeklyActivity() {
        List<WeeklyActivity> rankedRaces = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int rank = i + 1;
            WeeklyActivity activity = WeeklyActivity.builder()
                    .rank(rank)
                    .uid("test_id_" + rank)
                    .totalXp(500 * rank)
                    .name("test_name_" + rank)
                    .lastActivityTimestamp(1767214800000L + rank * 1000)
                    .build();
            rankedRaces.add(activity);
        }
        return rankedRaces;
    }

    public static List<WeeklyActivityDto> provideWeeklyActivityDtos() {
        WeeklyActivityMapper mapper = new WeeklyActivityMapperImpl();
        return mapper.toDto(provideWeeklyActivity());
    }

}
