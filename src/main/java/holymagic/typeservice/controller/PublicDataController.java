package holymagic.typeservice.controller;

import holymagic.typeservice.model.publicData.TypingStats;
import holymagic.typeservice.service.PublicDataService;
import holymagic.typeservice.validator.PublicDataRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicDataController {

    private final PublicDataService publicDataService;
    private final PublicDataRequestValidator publicDataRequestValidator;


    @Operation(summary = "gets number of users personal bests grouped by wpm level (multiples of ten)")
    @GetMapping("/speedHistogram")
    public ResponseEntity<Map<String, Integer>> getSpeedHistogram(
            @RequestParam(required = false, defaultValue = "time") String mode,
            @RequestParam(required = false, defaultValue = "60") String mode2) {
        publicDataRequestValidator.validateModeCombination(mode, mode2);
        return ResponseEntity.ok(publicDataService.getSpeedHistogram(mode, mode2));
    }

    @Operation(summary = "Gets number of tests and time users spend typing")
    @GetMapping("/typingStats")
    public ResponseEntity<TypingStats> getTypingStats() {
        return ResponseEntity.ok(publicDataService.getTypingStats());
    }

}
