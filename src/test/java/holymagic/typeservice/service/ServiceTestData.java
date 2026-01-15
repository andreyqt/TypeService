package holymagic.typeservice.service;

import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.dto.RankedRaceDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.mapper.RaceMapper;
import holymagic.typeservice.mapper.RaceMapperImpl;
import holymagic.typeservice.mapper.RankedRaceMapper;
import holymagic.typeservice.mapper.RankedRaceMapperImpl;
import holymagic.typeservice.mapper.WeeklyActivityMapper;
import holymagic.typeservice.mapper.WeeklyActivityMapperImpl;
import holymagic.typeservice.model.leaderboard.RankedRace;
import holymagic.typeservice.model.leaderboard.WeeklyActivity;
import holymagic.typeservice.model.publicData.TypingStats;
import holymagic.typeservice.model.race.PersonalBest;
import holymagic.typeservice.model.race.Race;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceTestData {

    public static final Race OUTDATED_RACE = Race.builder()
            .acc(100.)
            .wpm(200.50)
            .uid("abcD1234")
            .id("fi4G35d345")
            .mode("time")
            .mode2("15")
            .charStats(new int[]{175, 0, 0, 0})
            .timestamp(1767014108000L)
            .build();

    public static final Race NEW_RACE = Race.builder()
            .acc(99.)
            .wpm(201.50)
            .uid("abg43cD1234")
            .id("fi4G35dv3345")
            .mode("time")
            .mode2("15")
            .charStats(new int[]{176, 0, 0, 0})
            .timestamp(1767014115000L)
            .build();

    public static List<Race> provideRaces() {
        Race race1 = Race.builder()
                .acc(100.)
                .wpm(101.50)
                .uid("abc1234")
                .id("fi35d345")
                .testDuration(19.90)
                .mode("quote")
                .mode2("medium")
                .charStats(new int[]{75, 0, 0, 0})
                .timestamp(1767014109000L)
                .build();
        Race race2 = Race.builder()
                .acc(99.5)
                .wpm(102.38)
                .uid("3jk38k3")
                .id("298k39g0")
                .testDuration(18.98)
                .mode("quote")
                .mode2("short")
                .charStats(new int[]{49, 0, 0, 1})
                .timestamp(1767014110000L)
                .build();
        Race race3 = Race.builder()
                .acc(85.8)
                .wpm(97.358)
                .uid("34in3gn3")
                .id("3in60nf9")
                .mode("words")
                .mode2("25")
                .charStats(new int[]{57, 0, 0, 0})
                .timestamp(1767014120000L)
                .build();
        Race race4 = Race.builder()
                .acc(84.8)
                .wpm(97.358)
                .uid("314in3gn3")
                .id("3in360nf9")
                .mode("words")
                .mode2("50")
                .charStats(new int[]{107, 0, 0, 0})
                .timestamp(1767014130000L)
                .build();
        Race race5 = Race.builder()
                .acc(93.8)
                .wpm(107.358)
                .uid("614in3gn3")
                .id("35in360nf9")
                .mode("words")
                .mode2("10")
                .charStats(new int[]{39, 0, 0, 0})
                .timestamp(1767014140000L)
                .build();
        return List.of(race1, race2, race3, race4, race5);
    }

    public static List<RaceDto> provideRaceDtos() {
        RaceMapper mapper = new RaceMapperImpl();
        return mapper.toDto(provideRaces());
    }

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

    public static Map<String, Integer> provideHistogram() {
        Map<String, Integer> histogram = new HashMap<>();
        histogram.put("0", 144);
        histogram.put("10", 927);
        histogram.put("20", 3404);
        histogram.put("30", 8860);
        histogram.put("40", 17053);
        histogram.put("50", 28205);
        return histogram;
    }

    public static TypingStats provideTypingStats() {
        return TypingStats.builder()
                .testsCompleted(100L)
                .testsStarted(120L)
                .timeTyping(5.)
                .build();
    }

    public static Map<String, List<PersonalBest>> providePersonalBestsForTimeMode() {
        List<PersonalBest> pb15 = new ArrayList<>();

        Map<String, List<PersonalBest>> personalBests = new HashMap<>();
        return personalBests;
    }

    private static List<PersonalBest> generatePersonalBests() {
        List<PersonalBest> pbs = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            PersonalBest pb = PersonalBest.builder()
                    .timestamp(1767214800000L + i * 1000)
                    .wpm(100 + Math.random() * i)
                    .acc(100 - Math.random() * i)
                    .consistency(80 + Math.random() * i)
                    .difficulty("normal")
                    .punctuation(false)
                    .numbers(false)
                    .language("english")
                    .build();
            pbs.add(pb);
        }
        return pbs;
    }

}
