package holymagic.typeservice.service;

import holymagic.typeservice.dto.RankedRaceDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.mapper.RankedRaceMapper;
import holymagic.typeservice.mapper.RankedRaceMapperImpl;
import holymagic.typeservice.mapper.WeeklyActivityMapper;
import holymagic.typeservice.mapper.WeeklyActivityMapperImpl;
import holymagic.typeservice.model.Response;
import holymagic.typeservice.model.leaderboard.Leaderboard;
import holymagic.typeservice.model.leaderboard.RankedRace;
import holymagic.typeservice.model.leaderboard.WeeklyActivity;
import holymagic.typeservice.model.leaderboard.XpLeaderboard;

import java.net.URI;
import java.util.List;

public class LeaderboardServiceTestData {

    public static String EXPECTED_NOT_FOUND_EXCEPTION_MSG = String.format(
            "could not get the leaderboard for args: language=%s, mode=%s, mode2=%s, page=%s, pageSize=%s, friendsOnly=%s",
            "english", "time", "60", null, null, null);
    public static String EXPECTED_NOT_FOUND_EXCEPTION_RANK_MSG = String.format(
            "could not get the rank for args: language=%s, mode=%s, mode2=%s, friendsOnly=%s",
            "english", "time", "60", null);

    public static final URI EXPECTED_GET_LEADERBOARD_URI =
            URI.create("/leaderboards?language=english&mode=time&mode2=60");
    public static final URI EXPECTED_GET_DAILY_LEADERBOARD_URI =
            URI.create("/leaderboards/daily?language=english&mode=time&mode2=60");
    public static final URI EXPECTED_GET_LEADERBOARD_URI_WITH_ALL_PARAMS =
            URI.create("/leaderboards?language=english&mode=time&mode2=60&page=0&pageSize=3&friendsOnly=true");
    public static final URI EXPECTED_GET_RANK_URI =
            URI.create("/leaderboards/rank?language=english&mode=time&mode2=60");
    public static final URI EXPECTED_GET_XP_LEADERBOARD_URI =
            URI.create("/leaderboards/xp/weekly");

    public static final Response<Leaderboard> LEADERBOARD_RESPONSE =
            new Response<>("leaderboards retrieved", new Leaderboard(100, provideRankedRaces()));
    public static final Response<Leaderboard> LEADERBOARD_NULL_RESPONSE =
            new Response<>("leaderboards retrieved", null);
    public static final Response<Leaderboard> LEADERBOARD_FRIENDS_ONLY_RESPONSE =
            new Response<>("leaderboards retrieved", new Leaderboard(3, provideRankedRacesForFriendsOnly()));
    public static final Response<RankedRace> RANKED_RACE_RESPONSE =
            new Response<>("rank retrieved", provideRankedRaces().getFirst());
    public static final Response<RankedRace> RANKED_RACE_NULL_RESPONSE =
            new Response<>("rank retrieved", null);
    public static final Response<XpLeaderboard> XP_LEADERBOARD_RESPONSE =
            new Response<>("xp leaderboards retrieved", new XpLeaderboard(3, provideWeeklyActivities()));

    public static List<RankedRace> provideRankedRaces() {
        RankedRace firstRace = RankedRace.builder()
                .rank(1)
                .timestamp(1767014109000L)
                .name("test_name_1")
                .wpm(200.)
                .consistency(88.5)
                .raw(207.)
                .uid("3d4g4g54ye0")
                .acc(97.3)
                .discordId("3cg94323xc34")
                .discordAvatar("3ck-43=4nx")
                .build();
        RankedRace secondRace = RankedRace.builder()
                .rank(2)
                .timestamp(1767014101000L)
                .name("test_name_2")
                .wpm(198.2)
                .consistency(87.5)
                .raw(204.1)
                .uid("3d4g4g54ye0")
                .acc(97.89)
                .discordId("30jg0923bn4")
                .discordAvatar("3ck-43=4jo4j")
                .build();
        RankedRace thirdRace = RankedRace.builder()
                .rank(3)
                .timestamp(1767014119000L)
                .name("test_name")
                .wpm(193.)
                .consistency(88.5)
                .raw(201.)
                .uid("3d4g4g54ye0")
                .acc(96.3)
                .discordId("rjg34i9n34kg")
                .discordAvatar("3ck-342zjg34d4nx")
                .build();
        return List.of(firstRace, secondRace, thirdRace);
    }

    public static List<RankedRaceDto> provideRankedRacesDto() {
        RankedRaceMapper mapper = new RankedRaceMapperImpl();
        return provideRankedRaces().stream().map(mapper::toDto).toList();
    }

    public static List<RankedRace> provideRankedRacesForFriendsOnly() {
        RankedRace firstRace = RankedRace.builder()
                .rank(473)
                .timestamp(1767014109000L)
                .name("test_name_1")
                .wpm(120.)
                .consistency(88.5)
                .raw(120.)
                .uid("3d4g4g54ye0")
                .acc(97.3)
                .discordId("324gh4on4")
                .discordAvatar("5n4h50f09")
                .friendsRank(1)
                .build();
        RankedRace secondRace = RankedRace.builder()
                .rank(499)
                .timestamp(1767014101000L)
                .name("test_name_2")
                .wpm(118.2)
                .consistency(87.5)
                .raw(118.9)
                .uid("3d4g4g54ye0")
                .acc(97.89)
                .discordId("4gn450vbn3")
                .discordAvatar("1k660b3275")
                .friendsRank(2)
                .build();
        RankedRace thirdRace = RankedRace.builder()
                .rank(527)
                .timestamp(1767014119000L)
                .name("test_name")
                .wpm(109.95)
                .consistency(88.5)
                .raw(111.)
                .uid("3d4g4g54ye0")
                .acc(96.3)
                .discordId("5jy40fj243h")
                .discordAvatar("3lfj2-4uyn")
                .friendsRank(3)
                .build();
        return List.of(firstRace, secondRace, thirdRace);
    }

    public static List<RankedRaceDto> provideRankedRacesDtoForFriendsOnly() {
        RankedRaceMapper mapper = new RankedRaceMapperImpl();
        return provideRankedRacesForFriendsOnly().stream().map(mapper::toDto).toList();
    }

    public static RankedRaceDto provideRankedRaceDto() {
        return provideRankedRacesDto().getFirst();
    }

    public static List<WeeklyActivity> provideWeeklyActivities() {
        WeeklyActivity firstActivity = WeeklyActivity.builder()
                .uid("3gjn3094")
                .name("test_name")
                .discordId("234g934593")
                .discordAvatar("430-bn039h4g")
                .totalXp(539523)
                .rank(1)
                .lastActivityTimestamp(1764536400000L)
                .build();
        WeeklyActivity secondActivity = WeeklyActivity.builder()
                .uid("3gjn33094")
                .name("test_name_1")
                .discordId("2344g934593")
                .discordAvatar("4330-bn039h4g")
                .totalXp(539500)
                .rank(2)
                .lastActivityTimestamp(1764536410000L)
                .build();
        WeeklyActivity thirdActivity = WeeklyActivity.builder()
                .uid("3gjn5094")
                .name("test_name_2")
                .discordId("2344g934593")
                .discordAvatar("4330-bn039h4g")
                .totalXp(538500)
                .rank(3)
                .lastActivityTimestamp(1764536420000L)
                .build();
        return List.of(firstActivity, secondActivity, thirdActivity);
    }

    public static List<WeeklyActivityDto> provideWeeklyActivitiesDto() {
        WeeklyActivityMapper mapper = new WeeklyActivityMapperImpl();
        return provideWeeklyActivities().stream().map(mapper::toDto).toList();
    }

}
