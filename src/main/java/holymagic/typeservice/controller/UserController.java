package holymagic.typeservice.controller;

import holymagic.typeservice.dto.CurrentTestActivityDto;
import holymagic.typeservice.dto.PersonalBestDto;
import holymagic.typeservice.dto.StreakDto;
import holymagic.typeservice.model.user.CheckName;
import holymagic.typeservice.model.user.Profile;
import holymagic.typeservice.model.user.UserStats;
import holymagic.typeservice.service.UserService;
import holymagic.typeservice.validator.UserRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserRequestValidator validator;

    @Operation(summary = "Checks if the username available")
    @GetMapping("/checkName/{name}")
    public ResponseEntity<CheckName> checkName(@PathVariable String name) {
        return ResponseEntity.ok(userService.checkName(name));
    }

    @Operation(summary = "Gets user's personal bests for certain mode and language")
    @GetMapping("/personalBests")
    public ResponseEntity<List<PersonalBestDto>> getPersonalBests(
            @RequestParam(required = false, defaultValue = "time") String mode,
            @RequestParam(required = false, defaultValue = "60") String mode2,
            @RequestParam(required = false, defaultValue = "english") String language) {
        validator.validateModeCombination(mode, mode2);
        validator.validateLanguage(language);
        return ResponseEntity.ok(userService.getPersonalBests(mode, mode2, language));
    }

    @Operation(summary = "Gets user's personal bests for time/words modes and language")
    @GetMapping("/personalBests/all")
    public ResponseEntity<List<PersonalBestDto>> getAllPersonalBests(
            @RequestParam(required = false, defaultValue = "english") String language) {
        validator.validateLanguage(language);
        return ResponseEntity.ok(userService.getAllPersonalBestDtos(language));
    }

    @Operation(summary = "Gets and saves or updates personal bests for time and words mode")
    @PostMapping("/personalBests/save")
    public ResponseEntity<String> savePersonalBests(
            @RequestParam(required = false, defaultValue = "english") String language) {
        validator.validateLanguage(language);
        userService.savePersonalBests(language);
        return ResponseEntity.ok("Saved personal bests");
    }

    @Operation(summary = "Gets a user's typing stats data")
    @GetMapping("/stats")
    public ResponseEntity<UserStats> getStats() {
        return ResponseEntity.ok(userService.getUserStats());
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
