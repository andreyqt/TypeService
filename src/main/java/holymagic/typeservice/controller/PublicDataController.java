package holymagic.typeservice.controller;

import holymagic.typeservice.model.publicData.TypingStats;
import holymagic.typeservice.service.PublicDataService;
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

    @GetMapping("/speedHistogram")
    public ResponseEntity<Map<String, Integer>> getSpeedHistogram(@RequestParam String language,
                                                                @RequestParam String mode,
                                                                @RequestParam String mode2) {
        return ResponseEntity.ok(publicDataService.getSpeedHistogram(language, mode, mode2));
    }

    @GetMapping("/typingStats")
    public ResponseEntity<TypingStats> getTypingStats() {
        return ResponseEntity.ok(publicDataService.getTypingStats());
    }
}
