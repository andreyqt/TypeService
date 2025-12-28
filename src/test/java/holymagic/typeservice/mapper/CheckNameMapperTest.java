package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.CheckNameDto;
import holymagic.typeservice.model.CheckName;
import holymagic.typeservice.model.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class CheckNameMapperTest {

    private CheckNameMapper mapper;
    private CheckNameDto expectedCheckNameDto;
    private Response<CheckName> response;

    @BeforeEach
    public void setUp() {
        mapper = Mappers.getMapper(CheckNameMapper.class);
        expectedCheckNameDto = CheckNameDto.builder()
                .message("test message")
                .isAvailable(true)
                .build();
        response = new Response<>();
        response.setData(new CheckName(true));
        response.setMessage("test message");
    }

    @Test
    public void toTrueCheckNameDto() {
        CheckNameDto actualCheckNameDto = mapper.toDto(response);
        assertEquals(expectedCheckNameDto, actualCheckNameDto);
    }

    @Test
    public void toFalseCheckNameDto() {
        expectedCheckNameDto.setIsAvailable(false);
        response.setData(new CheckName(false));
        CheckNameDto actualCheckNameDto = mapper.toDto(response);
        assertEquals(expectedCheckNameDto, actualCheckNameDto);
    }
}
