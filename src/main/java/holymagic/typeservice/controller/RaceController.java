package holymagic.typeservice.controller;

import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.service.RaceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/races")
@RequiredArgsConstructor
public class RaceController {
    private final RaceService raceService;

    @Operation(summary = "Gets up to 1000 results, if all params are null, then returns races from cache")
    @GetMapping("/")
    public ResponseEntity<List<RaceDto>> getResults(@RequestParam(required = false) Long onOrAfterTimestamp,
                                                    @RequestParam(required = false) Integer offset,
                                                    @RequestParam(required = false) Integer limit) {
        return ResponseEntity.ok(raceService.getResults(onOrAfterTimestamp, offset, limit));
    }

    @Operation(summary = "Gets result by id")
    @GetMapping("/{id}")
    public ResponseEntity<RaceDto> getRaceById(@PathVariable String id) {
        return ResponseEntity.ok(raceService.getResultById(id));
    }

    @Operation(summary = "Gets result by timestamp from cache")
    @GetMapping("/timestamp/{timestamp}")
    public ResponseEntity<RaceDto> getRaceByTimestamp(@PathVariable Long timestamp) {
        return ResponseEntity.ok(raceService.getRaceByTimestampFromCache(timestamp));
    }

    @Operation(summary = "Gets a user's last saved result")
    @GetMapping("/last")
    public ResponseEntity<RaceDto> getLastResult() {
        return ResponseEntity.ok(raceService.getLastResult());
    }

    @Operation(summary = "Gets race from db by id")
    @GetMapping("/db/id/{id}")
    public ResponseEntity<RaceDto> getRaceFromDbById(@PathVariable String id) {
        return ResponseEntity.ok(raceService.getRaceByIdFromDb(id));
    }

    @Operation(summary = "Gets race from db by timestamp")
    @GetMapping("/db/timestamp/{timestamp}")
    public ResponseEntity<RaceDto> getRaceFromDbByTimestamp(@PathVariable Long timestamp) {
        return ResponseEntity.ok(raceService.getRaceByTimestampFromDb(timestamp));
    }

    @Operation(summary = "Saves race by id from cache into db")
    @PostMapping("/save/id/{id}")
    public ResponseEntity<RaceDto> saveRace(@PathVariable String id) {
        return ResponseEntity.ok(raceService.saveRace(id));
    }

    @Operation(summary = "Gets race from cache and saves into db")
    @PostMapping("/save/timestamp/{timestamp}")
    public ResponseEntity<RaceDto> saveRace(@PathVariable Long timestamp) {
        return ResponseEntity.ok(raceService.saveRace(timestamp));
    }

    @Operation(summary = "Saves all races from cache to db")
    @PostMapping("/save/all")
    public ResponseEntity<String> saveAllRaces() {
        raceService.saveAllRacesFromCache();
        return ResponseEntity.ok("All races were saved");
    }

    @Operation(summary = "Deletes race by id from db")
    @DeleteMapping("/delete/id/{id}")
    public ResponseEntity<String> deleteRace(@PathVariable String id) {
        raceService.deleteRace(id);
        return ResponseEntity.ok("race with id " + id + " was deleted");
    }

    @Operation(summary = "Deletes race by timestamp from db")
    @DeleteMapping("/delete/timestamp/{timestamp}")
    public ResponseEntity<String> deleteRace(@PathVariable Long timestamp) {
        raceService.deleteRace(timestamp);
        return ResponseEntity.ok("race with timestamp " + timestamp + " was deleted");
    }

}
