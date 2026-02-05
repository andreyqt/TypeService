package holymagic.typeservice.service;

import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.service.excel.ResultExcelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ResultExcelServiceTest {

    private ResultExcelService excelService;
    private RaceDto raceDto;
    private String testFilePath;

    @BeforeEach
    public void setUp() {
        excelService = new ResultExcelService();
        testFilePath = "C:\\Users\\User\\Documents\\MTT\\TS_test.xlsx";
        raceDto = RaceDto.builder()
                .id("1s23")
                .chars(400)
                .accuracy(100.)
                .speed(100.)
                .mode("quote")
                .mode2("short")
                .localDateTime(LocalDateTime.of(2025, 1, 2, 11, 46, 24))
                .testDuration(50.01)
                .build();
    }

    @Test
    public void readTest() {
        List<RaceDto> result = excelService.read(testFilePath);
        assertEquals(5, result.size());
        assertEquals(raceDto, result.getFirst());
    }

    @Test
    public void writeTest() {
        excelService.write(raceDto, testFilePath);
    }

    @Test
    public void writeListTest() {
        excelService.write(List.of(raceDto, raceDto, raceDto), testFilePath);
    }

}
