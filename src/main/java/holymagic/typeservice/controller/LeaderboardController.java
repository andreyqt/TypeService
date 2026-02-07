package holymagic.typeservice.controller;

import holymagic.typeservice.dto.RankingDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.service.LeaderboardService;
import holymagic.typeservice.validator.LeaderboardRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/leaderboards")
public class LeaderboardController {

    private final LeaderboardService leaderboardService;
    private final LeaderboardRequestValidator leaderboardRequestValidator;

    @Operation(summary = "Gets all-time leaderboard")
    @GetMapping("/")
    public ResponseEntity<List<RankingDto>> getLeaderboard(
            @RequestParam(required = false, defaultValue = "english") String language,
            @RequestParam(required = false, defaultValue = "time") String mode,
            @RequestParam(required = false, defaultValue = "60") String mode2,
            @RequestParam(required = false, defaultValue = "false") boolean friendsOnly) {
        leaderboardRequestValidator.validateGlobalLanguage(language);
        leaderboardRequestValidator.validateModes(mode, mode2);
        return ResponseEntity.ok(leaderboardService.getLeaderboard(language, mode, mode2, friendsOnly));
    }

    @Operation(summary = "Gets the rank of the current user on the all-time leaderboard")
    @GetMapping("/rank")
    public ResponseEntity<RankingDto> getRank(
            @RequestParam(required = false, defaultValue = "english") String language,
            @RequestParam(required = false, defaultValue = "time") String mode,
            @RequestParam(required = false, defaultValue = "60") String mode2,
            @RequestParam(required = false, defaultValue = "false") boolean friendsOnly) {
        leaderboardRequestValidator.validateGlobalLanguage(language);
        leaderboardRequestValidator.validateModes(mode, mode2);
        return ResponseEntity.ok(leaderboardService.getRank(language, mode, mode2, friendsOnly));
    }

    @Operation(summary = "Gets daily leaderboard")
    @GetMapping("/daily")
    public ResponseEntity<List<RankingDto>> getDailyLeaderboard(
            @RequestParam(required = false, defaultValue = "english") String language,
            @RequestParam(required = false, defaultValue = "time") String mode,
            @RequestParam(required = false, defaultValue = "60") String mode2,
            @RequestParam(required = false, defaultValue = "false") boolean friendsOnly) {
        leaderboardRequestValidator.validateDailyLanguage(language);
        leaderboardRequestValidator.validateModes(mode, mode2);
        return ResponseEntity.ok(leaderboardService.getDailyLeaderboard(language, mode, mode2, friendsOnly));
    }

    @Operation(summary = "Gets weekly xp leaderboard")
    @GetMapping("/xp/weekly")
    public ResponseEntity<List<WeeklyActivityDto>> getWeeklyXpLeaderboard(
            @RequestParam(required = false, defaultValue = "false") Boolean friendsOnly) {
        return ResponseEntity.ok(leaderboardService.getWeeklyXpLeaderboard(friendsOnly));
    }

}
