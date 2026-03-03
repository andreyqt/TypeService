package holymagic.typeservice.controller;

import holymagic.typeservice.model.result.DailyAverageResult;
import holymagic.typeservice.service.AnalyticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class AnalyticController {

    private final AnalyticService analyticService;

    @GetMapping("/today")
    public ResponseEntity<DailyAverageResult> getTodayAverage() {
        return ResponseEntity.ok(analyticService.getTodayAverage());
    }

}
