package holymagic.typeservice.model;

import holymagic.typeservice.model.race.PersonalBest;
import holymagic.typeservice.model.race.Race;
import holymagic.typeservice.model.user.CheckName;
import holymagic.typeservice.model.user.CurrentTestActivity;
import holymagic.typeservice.model.user.Profile;
import holymagic.typeservice.model.user.Stats;
import holymagic.typeservice.model.user.Streak;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;

public class ParameterizedTypeReferences {

    public static final ParameterizedTypeReference<Response<CheckName>> CHECK_NAME_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<Map<String, List<PersonalBest>>>> MAP_OF_LIST_OF_RECORDS =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<List<PersonalBest>>> LIST_OF_RECORDS =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<Stats>> STATS_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<Profile>> PROFILE_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<CurrentTestActivity>> CURRENT_TEST_ACTIVITY_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<Streak>> STREAK_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<List<Race>>> LIST_OF_RACES_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<Race>> RACE_REF
            = new ParameterizedTypeReference<>() {};
}
