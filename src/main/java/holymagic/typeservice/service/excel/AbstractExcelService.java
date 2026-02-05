package holymagic.typeservice.service.excel;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractExcelService<T> {

    protected final ConcurrentHashMap<String, Object> locks = new ConcurrentHashMap<>();

    public void write (T t, String filePath) {
        Object lock = locks.computeIfAbsent(filePath, k -> new Object());
        synchronized (lock) {

            try (FileInputStream fis = new FileInputStream(filePath);
                 XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

                XSSFSheet sheet = workbook.getSheetAt(0);
                int lastRowNum = sheet.getLastRowNum();
                Row newRow = sheet.createRow(lastRowNum + 1);
                writeToRow(newRow, t);
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    workbook.write(fos);
                }

            } catch (IOException e) {
                log.error("couldn't write race to file: {}", e.getMessage());
            } finally {
                locks.remove(filePath);
            }
        }
    }

    public void write(List<T> tList, String fileName) {
        Object lock = locks.computeIfAbsent(fileName, k -> new Object());
        synchronized (lock) {

            try (FileInputStream fis = new FileInputStream(fileName);
                 XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

                XSSFSheet sheet = workbook.getSheetAt(0);
                int startRow = sheet.getLastRowNum() + 1;
                log.info("startRow: {}", startRow);

                int rowIndex = startRow;
                for (T t : tList) {
                    Row row = sheet.createRow(rowIndex++);
                    writeToRow(row, t);
                }
                try (FileOutputStream fos = new FileOutputStream(fileName)) {
                    workbook.write(fos);
                }

            } catch (IOException e) {
                log.error("couldn't write races to file: {}", e.getMessage());
            } finally {
                locks.remove(fileName);
            }
        }
    }

    public List<T> read(String filePath) {
        Object lock = locks.computeIfAbsent(filePath, k -> new Object());
        synchronized (lock) {

            try (FileInputStream file = new FileInputStream(filePath);
                 XSSFWorkbook workbook = new XSSFWorkbook(file)) {

                List<T> tList = new ArrayList<>();
                XSSFSheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;
                    T t = readFromRow(row);
                    tList.add(t);
                }
                return tList;

            } catch (IOException e) {
                log.error(e.getMessage());
            } finally {
                locks.remove(filePath);
            }
        }
        return null;
    }

    public abstract T readFromRow(Row row);
    public abstract void writeToRow(Row row, T t);

}
