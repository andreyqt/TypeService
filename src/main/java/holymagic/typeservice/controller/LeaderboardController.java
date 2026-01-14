package holymagic.typeservice.controller;

import holymagic.typeservice.dto.RankedRaceDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.service.LeaderboardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/leaderboards")
public class LeaderboardController {
    private final LeaderboardService leaderboardService;

    @Operation(summary = "Gets all-time leaderboard")
    @GetMapping("/")
    public ResponseEntity<List<RankedRaceDto>> getLeaderboard(@RequestParam String language,
                                                              @RequestParam String mode,
                                                              @RequestParam String mode2,
                                                              @RequestParam(required = false) Integer page,
                                                              @RequestParam(required = false) Integer pageSize,
                                                              @RequestParam(required = false) Boolean friendsOnly) {
        return ResponseEntity.ok(leaderboardService.getLeaderboard(language, mode, mode2, page, pageSize, friendsOnly));
    }

    @Operation(summary = "Gets the rank of the current user on the all-time leaderboard")
    @GetMapping("/rank")
    public ResponseEntity<RankedRaceDto> getRank(@RequestParam String language,
                                                 @RequestParam String mode,
                                                 @RequestParam String mode2,
                                                 @RequestParam(required = false) Boolean friendsOnly) {
        return ResponseEntity.ok(leaderboardService.getRank(language, mode, mode2, friendsOnly));
    }

    @Operation(summary = "Gets daily leaderboard")
    @GetMapping("/daily")
    public ResponseEntity<List<RankedRaceDto>> getDailyLeaderboard(@RequestParam String language,
                                                                   @RequestParam String mode,
                                                                   @RequestParam String mode2,
                                                                   @RequestParam(required = false) Integer page,
                                                                   @RequestParam(required = false) Integer pageSize,
                                                                   @RequestParam(required = false) Boolean friendsOnly) {
        return ResponseEntity.ok(leaderboardService.getDailyLeaderboard(language, mode, mode2,
                page, pageSize, friendsOnly));
    }

    @Operation(summary = "Gets weekly xp leaderboard")
    @GetMapping("/xp/weekly")
    public ResponseEntity<List<WeeklyActivityDto>> getWeeklyXpLeaderboard(@RequestParam(required = false) Integer page,
                                                                          @RequestParam(required = false) Integer pageSize,
                                                                          @RequestParam(required = false) Boolean friendsOnly) {
        return ResponseEntity.ok(leaderboardService.getWeeklyXpLeaderboard(friendsOnly, page, pageSize));
    }

}
