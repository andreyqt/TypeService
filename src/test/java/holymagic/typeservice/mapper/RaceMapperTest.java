package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.model.race.Race;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RaceMapperTest {

    private RaceMapper mapper;
    private Race race;
    private RaceDto expectedRaceDto;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(RaceMapper.class);
        int[] charStats = {627, 0, 0, 0};
        race = Race.builder()
                .wpm(100.)
                .acc(100.)
                .mode("time")
                .mode2("30")
                .testDuration(29.99)
                .id("1abc")
                .uid("1abc1def")
                .charStats(charStats)
                .timestamp(1767014109000L)
                .build();
        expectedRaceDto = RaceDto.builder()
                .speed(100.)
                .accuracy(100.)
                .mode("time")
                .mode2("30")
                .testDuration(29.99)
                .chars(627)
                .id("1abc")
                .uid("1abc1def")
                .localDateTime(LocalDateTime.of(2025, 12, 29, 16, 15, 9))
                .build();
    }

    @Test
    public void toRaceDtoTest() {
        RaceDto actualRaceDto = mapper.toDto(race);
        assertEquals(expectedRaceDto, actualRaceDto);
    }
}
