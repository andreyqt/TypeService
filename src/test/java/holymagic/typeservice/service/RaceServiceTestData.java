package holymagic.typeservice.service;

import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.mapper.RaceMapper;
import holymagic.typeservice.mapper.RaceMapperImpl;
import holymagic.typeservice.model.Response;
import holymagic.typeservice.model.race.Race;

import java.net.URI;
import java.util.List;

public class RaceServiceTestData {

    public static final Response<List<Race>> RESPONSE_OF_RACES =
            new Response<>("test message", provideRaces());
    public static final Response<List<Race>> RESPONSE_WITH_NULL =
            new Response<>("null response", null);
    public static final Response<Race> RESPONSE_WITH_SINGLE_RESULT =
            new Response<>("single race", provideRaces().getFirst());
    public static final Response<Race> SINGLE_RESPONSE_WITH_NULL =
            new Response<>("null response", null);

    public static final List<RaceDto> EXPECTED_LIST_OF_DTO_RACES = provideListOfDtoRaces();
    public static final RaceDto EXPECTED_RACE_DTO = provideListOfDtoRaces().getFirst();
    public static final URI EXPECTED_GET_RESULTS_URI = URI.create("/results");
    public static final URI EXPECTED_GET_RESULTS_WITH_PARAMS_URI =
            URI.create("/results?onOrAfterTimestamp=1589428800000&offset=0&limit=3");
    public static final URI EXPECTED_GET_BY_ID_RESULT_URI = URI.create("/results/id/fi35d345");
    public static final URI EXPECTED_GET_LAST_RESULT_URI = URI.create("/results/last");

    public static List<Race> provideRaces() {
        Race race1 = Race.builder()
                .acc(100.)
                .wpm(101.50)
                .uid("abc1234")
                .id("fi35d345")
                .testDuration(19.90)
                .mode("quote")
                .mode2("medium")
                .charStats(new int[]{75,0,0,0})
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
                .charStats(new int[]{49,0,0,1})
                .timestamp(1767014110000L)
                .build();
        Race race3 = Race.builder()
                .acc(85.8)
                .wpm(97.358)
                .uid("34in3gn3")
                .id("3in60nf9")
                .mode("words")
                .mode2("25")
                .charStats(new int[]{57,0,0,0})
                .timestamp(1767014120000L)
                .build();
        return List.of(race1, race2, race3);
    }

    public static List<RaceDto> provideListOfDtoRaces() {
        RaceMapper raceMapper = new RaceMapperImpl();
        return raceMapper.toDto(provideRaces());
    }

}
