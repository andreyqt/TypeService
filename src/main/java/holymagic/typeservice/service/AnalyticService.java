package holymagic.typeservice.service;

import holymagic.typeservice.dto.ResultDto;
import holymagic.typeservice.exception.DataValidationException;
import holymagic.typeservice.model.result.DailyAverageResult;
import holymagic.typeservice.repository.DailyResultRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticService {

    private final ResultService resultService;
    private final DailyResultRepository dailyResultRepository;
    private DailyAverageResult dailyAverageResult;

    public DailyAverageResult getTodayAverage() {
        long todayMidnightTimestamp = LocalDate.now()
                .atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();

        List<ResultDto> todayResults = resultService.getResults(todayMidnightTimestamp, 1000)
                .stream()
                .filter(resultDto -> resultDto.getMode().equals("quote"))
                .collect(Collectors.toList());

        dailyAverageResult = calculateDailyAverage(todayResults, LocalDate.now());
        return dailyAverageResult;
    }

    @Transactional
    public void saveDailyAverageResult() {
        validateDailyResult();
        dailyResultRepository.save(dailyAverageResult);
    }

    @Transactional(readOnly = true)
    public DailyAverageResult getDailyAverageResult(LocalDate date) {
        return dailyResultRepository.findByDate(date)
                .orElseThrow(() -> new EntityNotFoundException("no result for the given date " + date));
    }

    private DailyAverageResult calculateDailyAverage(List<ResultDto> todayResults, LocalDate localDate) {
        DoubleSummaryStatistics wmpStats = todayResults.stream()
                .mapToDouble(ResultDto::getSpeed)
                .summaryStatistics();

        DoubleSummaryStatistics accStats = todayResults.stream()
                .mapToDouble(ResultDto::getAccuracy)
                .summaryStatistics();

        double totalTime = todayResults.stream()
                .mapToDouble(ResultDto::getTestDuration)
                .sum()/60.0;

        return DailyAverageResult.builder()
                .localDate(localDate)
                .averageAcc(roundToTwoDecimals(accStats.getAverage()))
                .averageWmp(roundToTwoDecimals(wmpStats.getAverage()))
                .time(roundToTwoDecimals(totalTime))
                .numOfTests(todayResults.size())
                .build();
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private void validateDailyResult() {
        if (dailyAverageResult.getNumOfTests() < 100) {
            throw new DataValidationException("not enough test completed to save results: " +
                                              dailyAverageResult.getNumOfTests());
        }
    }

}
