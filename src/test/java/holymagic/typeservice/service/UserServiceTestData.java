package holymagic.typeservice.service;

import holymagic.typeservice.dto.CheckNameDto;
import holymagic.typeservice.dto.PersonalBestDto;
import holymagic.typeservice.mapper.PersonalBestMapper;
import holymagic.typeservice.mapper.PersonalBestMapperImpl;
import holymagic.typeservice.model.CheckName;
import holymagic.typeservice.model.Response;
import holymagic.typeservice.model.race.PersonalBest;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class UserServiceTestData {
    public static final String NAME_TO_CHECK = "test_name";

    public static final Response<CheckName> CHECK_NAME_RESPONSE =
            new Response<>("test message", new CheckName(true));
    public static final Response<List<PersonalBest>> LIST_OF_RECORDS_RESPONSE_FOR_30S =
            new Response<>("test message", List.of(providePersonalBestsForTimeMode().get(1)));
    public static final Response<Map<String, List<PersonalBest>>> MAP_OF_LIST_OF_RECORDS_RESPONSE =
            new Response<>("test message", providePersonalBests());

    public static final CheckNameDto EXPECTED_CHECK_NAME_DTO =
            new CheckNameDto("test message", true);
    public static final List<PersonalBestDto> EXPECTED_DTO_RECORDS_FOR_30S =
            List.of(providePersonalBestDtoForTimeMode().get(1));
    public static final Map<String, List<PersonalBestDto>> EXPECTED_DTO_MAP_OF_LIST_OF_RECORDS =
            providePersonalBestDto();

    public static final URI EXPECTED_CHECK_NAME_URI = URI.create("users/checkName/test_name");
    public static final URI EXPECTED_GET_PERSONAL_BEST_URI = URI.create("/users/personalBests?mode=time");
    public static final URI EXPECTED_GET_PERSONAL_BEST_30S_URI = URI.create("/users/personalBests?mode=time&mode2=30");

    public static List<PersonalBest> providePersonalBestsForTimeMode() {
        PersonalBest pb15s = PersonalBest.builder()
                .acc(100.)
                .wpm(171.18)
                .language("english")
                .timestamp(1764000248425L)
                .build();
        PersonalBest pb30s = PersonalBest.builder()
                .acc(99.75)
                .wpm(156.8)
                .language("english")
                .timestamp(1752597178773L)
                .build();
        PersonalBest pb60s = PersonalBest.builder()
                .acc(97.31)
                .wpm(149.2)
                .language("english")
                .timestamp(1762545393692L)
                .build();
        PersonalBest pb120s = PersonalBest.builder()
                .acc(99.3)
                .wpm(141.8)
                .language("english")
                .timestamp(1758805875576L)
                .build();
        return List.of(pb15s, pb30s, pb60s, pb120s);
    }

    public static List<PersonalBest> providePersonalBestsForWordsMode() {
        PersonalBest pb10w = PersonalBest.builder()
                .acc(100.)
                .wpm(211.05)
                .language("english")
                .timestamp(1759406226564L)
                .build();
        PersonalBest pb25w = PersonalBest.builder()
                .acc(100.)
                .wpm(187.51)
                .language("english")
                .timestamp(1765298631899L)
                .build();
        PersonalBest pb50w = PersonalBest.builder()
                .acc(99.22)
                .wpm(172.05)
                .language("english")
                .timestamp(1760005920338L)
                .build();
        PersonalBest pb100w = PersonalBest.builder()
                .acc(100.)
                .wpm(128.18)
                .language("english")
                .timestamp(1696437976466L)
                .build();
        return List.of(pb10w, pb25w, pb50w, pb100w);
    }

    public static Map<String, List<PersonalBest>> providePersonalBests() {
        return Map.of(
                "time", providePersonalBestsForTimeMode(),
                "words", providePersonalBestsForWordsMode()
        );
    }

    public static List<PersonalBestDto> providePersonalBestDtoForTimeMode() {
        PersonalBestMapper personalBestMapper = new PersonalBestMapperImpl();
        return personalBestMapper.toDto(providePersonalBestsForTimeMode());
    }

    public static List<PersonalBestDto> providePersonalBestDtoForWordsMode() {
        PersonalBestMapper personalBestMapper = new PersonalBestMapperImpl();
        return personalBestMapper.toDto(providePersonalBestsForWordsMode());
    }

    public static Map<String, List<PersonalBestDto>> providePersonalBestDto() {
        return Map.of(
                "time", providePersonalBestDtoForTimeMode(),
                "words", providePersonalBestDtoForWordsMode()
        );
    }

}
