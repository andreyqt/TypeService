package holymagic.typeservice.controller;

import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.service.RaceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/races")
@RequiredArgsConstructor
public class RaceController {
    private final RaceService raceService;

    @Operation(summary = "Gets up to 1000 results")
    @GetMapping("/")
    public ResponseEntity<List<RaceDto>> getResults(@RequestParam(required = false) Long onOrAfterTimestamp,
                                                    @RequestParam(required = false) Integer offset,
                                                    @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(raceService.getResults(onOrAfterTimestamp, offset, limit));
    }

    @Operation(summary = "Get result by id")
    @GetMapping("/{id}")
    public ResponseEntity<RaceDto> getRaceById(@PathVariable String id) {
        return ResponseEntity.ok(raceService.getResultById(id));
    }

    @Operation(summary = "Gets a user's last saved result")
    @GetMapping("/last")
    public ResponseEntity<RaceDto> getLastResult() {
        return ResponseEntity.ok(raceService.getLastResult());
    }
}
