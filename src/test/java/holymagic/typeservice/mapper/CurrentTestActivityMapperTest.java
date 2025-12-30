package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.CurrentTestActivityDto;
import holymagic.typeservice.model.user.CurrentTestActivity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CurrentTestActivityMapperTest {

    private CurrentTestActivityMapper mapper;
    private CurrentTestActivity currentTestActivity;
    private CurrentTestActivityDto currentTestActivityDto;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(CurrentTestActivityMapper.class);
        currentTestActivity = new CurrentTestActivity(List.of(4, 5, 6, 7, 8, 9), 1767052800000L);
        currentTestActivityDto = CurrentTestActivityDto.builder()
                .testsByDays(List.of(4, 5, 6, 7, 8, 9))
                .lastDay(LocalDateTime.of(2025, 12, 30, 3, 0, 0))
                .build();
    }

    @Test
    public void toDtoTest() {
        CurrentTestActivityDto actualTestActivityDto = mapper.toDto(currentTestActivity);
        assertEquals(currentTestActivityDto, actualTestActivityDto);
    }
}
