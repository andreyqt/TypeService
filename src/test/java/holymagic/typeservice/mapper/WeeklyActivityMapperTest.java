package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.model.leaderboard.WeeklyActivity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class WeeklyActivityMapperTest {

    private WeeklyActivityMapper mapper;
    private WeeklyActivity weeklyActivity;
    private WeeklyActivityDto weeklyActivityDto;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(WeeklyActivityMapper.class);
        weeklyActivity = WeeklyActivity.builder()
                .uid("3gjn3094")
                .name("test_name")
                .discordId("234g934593")
                .discordAvatar("430-bn039h4g")
                .rank(353)
                .lastActivityTimestamp(1764536400000L)
                .build();
        weeklyActivityDto = WeeklyActivityDto.builder()
                .uid("3gjn3094")
                .name("test_name")
                .discordId("234g934593")
                .rank(353)
                .lastActivityTimestamp(LocalDateTime.of(2025, 12,1, 0,0,0))
                .build();
    }

    @Test
    public void toDtoTest() {
        WeeklyActivityDto actualWeeklyActivityDto = mapper.toDto(weeklyActivity);
        assertEquals(weeklyActivityDto, actualWeeklyActivityDto);
    }
}
