package holymagic.typeservice.service;

import holymagic.typeservice.dto.ResultDto;
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
    private ResultDto resultDto;
    private String readFilePath;
    private String writeFilePath;

    @BeforeEach
    public void setUp() {
        excelService = new ResultExcelService();
        readFilePath = "C:\\Users\\User\\Documents\\MTT\\TS_test_read.xlsx";
        writeFilePath = "C:\\Users\\User\\Documents\\MTT\\TS_test_write.xlsx";
        resultDto = ResultDto.builder()
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
        List<ResultDto> result = excelService.read(readFilePath);
        assertEquals(5, result.size());
        assertEquals(resultDto, result.getFirst());
    }

    @Test
    public void writeTest() {
        excelService.write(resultDto, writeFilePath);
    }

    @Test
    public void writeListTest() {
        excelService.write(List.of(resultDto, resultDto, resultDto), writeFilePath);
    }

}
