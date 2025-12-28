package holymagic.typeservice.controller;

import holymagic.typeservice.dto.CheckNameDto;
import holymagic.typeservice.dto.CurrentTestActivityDto;
import holymagic.typeservice.dto.PersonalBestDto;
import holymagic.typeservice.dto.StreakDto;
import holymagic.typeservice.model.user.Profile;
import holymagic.typeservice.model.user.Stats;
import holymagic.typeservice.service.UserService;
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

    @GetMapping("/checkName/{name}")
    public ResponseEntity<CheckNameDto> checkName(@PathVariable String name) {
        return ResponseEntity.ok(userService.checkName(name));
    }

    @GetMapping("/personalBests/{mode}")
    public ResponseEntity<Map<String, List<PersonalBestDto>>> getPersonalBests(@PathVariable String mode) {
        return ResponseEntity.ok(userService.getPersonalBests(mode));
    }

    @GetMapping("/personalBests/{mode}/{mode2}")
    public ResponseEntity<List<PersonalBestDto>> getPersonalBests(@PathVariable String mode,
                                                                  @PathVariable String mode2) {
        return ResponseEntity.ok(userService.getPersonalBests(mode, mode2));
    }

    @GetMapping("/stats")
    public ResponseEntity<Stats> getStats() {
        return ResponseEntity.ok(userService.getStats());
    }

    @GetMapping("/profile/{name}")
    public ResponseEntity<Profile> getProfile(@PathVariable String name) {
        return ResponseEntity.ok(userService.getProfile(name));
    }

    @GetMapping("/activity")
    public ResponseEntity<CurrentTestActivityDto> getCurrentTestActivity() {
        return ResponseEntity.ok(userService.getCurrentTestActivity());
    }

    @GetMapping("/streak")
    public ResponseEntity<StreakDto> getStreak() {
        return ResponseEntity.ok(userService.getStreak());
    }

}
