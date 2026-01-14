package holymagic.typeservice.service;

import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.mapper.RaceMapper;
import holymagic.typeservice.mapper.RaceMapperImpl;
import holymagic.typeservice.model.race.Race;

import java.util.List;

public class RaceTestData {

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

}
