package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.RankingDto;
import holymagic.typeservice.model.leaderboard.Ranking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RankingMapperTest {

    private RankingMapper rankedRaceMapper;
    private Ranking ranking;
    private RankingDto rankingDto;

    @BeforeEach
    void setUp() {
        rankedRaceMapper = Mappers.getMapper(RankingMapper.class);
        ranking = Ranking.builder()
                .rank(1)
                .timestamp(1767014109000L)
                .name("test_name")
                .wpm(200.)
                .consistency(88.5)
                .raw(207.)
                .uid("3d4g4g54ye0")
                .acc(97.3)
                .discordId("3cg94323xc34")
                .discordAvatar("3ck-43=4nx")
                .build();
        rankingDto = RankingDto.builder()
                .rank(1)
                .localDateTime(LocalDateTime.of(2025, 12, 29, 16, 15, 9))
                .name("test_name")
                .speed(200.)
                .uid("3d4g4g54ye0")
                .discordId("3cg94323xc34")
                .accuracy(97.3)
                .build();
    }

    @Test
    public void toDtoTest() {
        RankingDto actualRankingDto = rankedRaceMapper.toDto(ranking);
        assertEquals(rankingDto, actualRankingDto);
    }

}
