package holymagic.typeservice.controller;

import holymagic.typeservice.dto.CheckNameDto;
import holymagic.typeservice.dto.CurrentTestActivityDto;
import holymagic.typeservice.dto.PersonalBestDto;
import holymagic.typeservice.dto.StreakDto;
import holymagic.typeservice.model.user.CheckName;
import holymagic.typeservice.model.user.Profile;
import holymagic.typeservice.model.user.Stats;
import holymagic.typeservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Checks if the username available")
    @GetMapping("/checkName/{name}")
    public ResponseEntity<CheckName> checkName(@PathVariable String name) {
        return ResponseEntity.ok(userService.checkName(name));
    }

    @Operation(summary = "Gets user's personal bests")
    @GetMapping("/personalBests/{mode}")
    public ResponseEntity<Map<String, List<PersonalBestDto>>> getPersonalBests(@PathVariable String mode) {
        return ResponseEntity.ok(userService.getPersonalBests(mode));
    }

    @Operation(summary = "Gets a user's typing stats data")
    @GetMapping("/personalBests/{mode}/{mode2}")
    public ResponseEntity<List<PersonalBestDto>> getPersonalBests(@PathVariable String mode,
                                                                  @PathVariable String mode2) {
        return ResponseEntity.ok(userService.getPersonalBests(mode, mode2));
    }

    @Operation(summary = "Gets a user's typing stats data")
    @GetMapping("/stats")
    public ResponseEntity<Stats> getStats() {
        return ResponseEntity.ok(userService.getStats());
    }

    @Operation(summary = "Gets a user's profile")
    @GetMapping("/profile/{name}")
    public ResponseEntity<Profile> getProfile(@PathVariable String name) {
        return ResponseEntity.ok(userService.getProfile(name));
    }

    @Operation(summary = "Gets test activity for the last up to 372 days for the current user")
    @GetMapping("/activity")
    public ResponseEntity<CurrentTestActivityDto> getCurrentTestActivity() {
        return ResponseEntity.ok(userService.getCurrentTestActivity());
    }

    @Operation(summary = "Gets user's streak data")
    @GetMapping("/streak")
    public ResponseEntity<StreakDto> getStreak() {
        return ResponseEntity.ok(userService.getStreak());
    }

}
