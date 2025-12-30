package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.PersonalBestDto;
import holymagic.typeservice.model.race.PersonalBest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PersonalBestMapperTest {

    PersonalBestMapper personalBestMapper;
    PersonalBest testPersonalBest;
    PersonalBestDto testPersonalBestDto;

    @BeforeEach
    void setUp() {
        personalBestMapper = Mappers.getMapper(PersonalBestMapper.class);
        testPersonalBest = PersonalBest.builder()
                .acc(100.)
                .wpm(99.)
                .language("english")
                .numbers(false)
                .consistency(80.)
                .punctuation(false)
                .timestamp(1762545393000L)
                .build();
        testPersonalBestDto = PersonalBestDto.builder()
                .speed(99.)
                .accuracy(100.)
                .language("english")
                .localDateTime(LocalDateTime.of(2025,11,7,22,56, 33))
                .numbers(false)
                .punctuation(false)
                .build();
    }

    @Test
    public void toDtoTest() {
        PersonalBestDto actualPersonalBestDto = personalBestMapper.toDto(testPersonalBest);
        assertEquals(testPersonalBestDto, actualPersonalBestDto);
    }
}
