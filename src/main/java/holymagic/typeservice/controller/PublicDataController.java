package holymagic.typeservice.controller;

import holymagic.typeservice.model.publicData.TypingStats;
import holymagic.typeservice.service.PublicDataService;
import holymagic.typeservice.validator.LeaderboardRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/public")
@RequiredArgsConstructor
public class PublicDataController {

    private final PublicDataService publicDataService;
    private final LeaderboardRequestValidator validator;

    @Operation(summary = "gets number of users personal bests grouped by wpm level (multiples of ten)")
    @GetMapping("/speedHistogram")
    public ResponseEntity<Map<String, Integer>> getSpeedHistogram(@RequestParam String language,
                                                                @RequestParam String mode,
                                                                @RequestParam String mode2) {
        validator.ValidateGlobalLeaderboardArgs(language, mode, mode2, null, null);
        return ResponseEntity.ok(publicDataService.getSpeedHistogram(language, mode, mode2));
    }

    @Operation(summary = "gets number of tests and time users spend typing")
    @GetMapping("/typingStats")
    public ResponseEntity<TypingStats> getTypingStats() {
        return ResponseEntity.ok(publicDataService.getTypingStats());
    }
}
