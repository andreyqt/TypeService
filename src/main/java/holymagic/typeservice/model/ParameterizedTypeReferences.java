package holymagic.typeservice.model;

import holymagic.typeservice.model.leaderboard.Leaderboard;
import holymagic.typeservice.model.leaderboard.RankedRace;
import holymagic.typeservice.model.leaderboard.XpLeaderboard;
import holymagic.typeservice.model.publicData.TypingStats;
import holymagic.typeservice.model.result.Result;
import holymagic.typeservice.model.user.PersonalBest;
import holymagic.typeservice.model.race.Race;
import holymagic.typeservice.model.user.CheckName;
import holymagic.typeservice.model.user.CurrentTestActivity;
import holymagic.typeservice.model.user.Profile;
import holymagic.typeservice.model.user.UserStats;
import holymagic.typeservice.model.user.Streak;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;

public class ParameterizedTypeReferences {

    public static final ParameterizedTypeReference<Response<CheckName>> CHECK_NAME_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<Map<String, List<PersonalBest>>>> MAP_OF_RECORDS_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<List<PersonalBest>>> LIST_OF_RECORDS =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<UserStats>> USER_STATS_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<Profile>> PROFILE_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<CurrentTestActivity>> CURRENT_TEST_ACTIVITY_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<Streak>> STREAK_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<List<Result>>> LIST_OF_RESULTS_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<Result>> RESULT_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<Map<String, Integer>>> SPEED_HISTOGRAM_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<TypingStats>> TYPING_STATS_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<Leaderboard>> LEADERBOARD_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<RankedRace>> RANKED_RACE_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<XpLeaderboard>> XP_LEADERBOARD_REF =
            new ParameterizedTypeReference<>() {};
}
