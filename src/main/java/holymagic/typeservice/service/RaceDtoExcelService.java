package holymagic.typeservice.service;

import holymagic.typeservice.dto.RaceDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class RaceDtoExcelService extends AbstractExcelService<RaceDto> {

    @Override
    public void writeToRow(Row row, RaceDto dto) {
        Cell[] cells = new Cell[8];
        for (int i = 0; i < 8; i++) {
            cells[i] = row.createCell(i);
        }
        cells[0].setCellValue(dto.getId());
        cells[1].setCellValue(dto.getChars());
        cells[2].setCellValue(dto.getSpeed());
        cells[3].setCellValue(dto.getAccuracy());
        cells[4].setCellValue(dto.getMode());
        cells[5].setCellValue(dto.getMode2());
        cells[6].setCellValue(dto.getLocalDateTime().toString());
        cells[7].setCellValue(dto.getTestDuration());
    }

    @Override
    public RaceDto readFromRow(Row row) {
        return RaceDto.builder()
                .id(row.getCell(0).getStringCellValue())
                .chars((int) row.getCell(1).getNumericCellValue())
                .speed(row.getCell(2).getNumericCellValue())
                .accuracy(row.getCell(3).getNumericCellValue())
                .mode(row.getCell(4).getStringCellValue())
                .mode2(row.getCell(5).getStringCellValue())
                .localDateTime(LocalDateTime.parse(row.getCell(6).getStringCellValue()))
                .testDuration(row.getCell(7).getNumericCellValue())
                .build();
    }

}
