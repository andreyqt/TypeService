package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.StreakDto;
import holymagic.typeservice.model.user.Streak;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class StreakMapperTest {

    private StreakMapper mapper;
    private Streak streak;
    private StreakDto streakDto;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(StreakMapper.class);
        streak = Streak.builder()
                .lastResultTimestamp(1767128823000L)
                .length(455)
                .maxLength(460)
                .build();
        streakDto = StreakDto.builder()
                .lastResultTimestamp(LocalDateTime.of(2025, 12, 31, 0, 7, 3))
                .length(455)
                .maxLength(460)
                .build();
    }

    @Test
    public void toDtoTest() {
        StreakDto actualDto = mapper.ToDto(streak);
        assertEquals(streakDto, actualDto);
    }

}
