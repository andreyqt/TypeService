package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.ResultDto;
import holymagic.typeservice.model.result.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ResultMapperTest {

    private ResultMapper mapper;
    private Result result;
    private ResultDto expectedResultDto;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(ResultMapper.class);
        List<Integer> charStats = Arrays.asList(100,2,2,4);
        result = Result.builder()
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
        expectedResultDto = ResultDto.builder()
                .speed(100.)
                .accuracy(100.)
                .mode("time")
                .mode2("30")
                .testDuration(29.99)
                .chars(100)
                .id("1abc")
                .uid("1abc1def")
                .localDateTime(LocalDateTime.of(2025, 12, 29, 16, 15, 9))
                .build();
    }

    @Test
    public void toRaceDtoTest() {
        ResultDto actualResultDto = mapper.toDto(result);
        assertEquals(expectedResultDto, actualResultDto);
    }

}
