package holymagic.typeservice.service;

import holymagic.typeservice.dto.CurrentTestActivityDto;
import holymagic.typeservice.dto.PersonalBestDto;
import holymagic.typeservice.dto.StreakDto;
import holymagic.typeservice.mapper.CurrentTestActivityMapper;
import holymagic.typeservice.mapper.PersonalBestMapper;
import holymagic.typeservice.mapper.StreakMapper;
import holymagic.typeservice.model.race.PersonalBest;
import holymagic.typeservice.model.user.CheckName;
import holymagic.typeservice.model.user.CurrentTestActivity;
import holymagic.typeservice.model.user.Profile;
import holymagic.typeservice.model.user.UserStats;
import holymagic.typeservice.model.user.Streak;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static holymagic.typeservice.model.ParameterizedTypeReferences.CHECK_NAME_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.CURRENT_TEST_ACTIVITY_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.LIST_OF_RECORDS;
import static holymagic.typeservice.model.ParameterizedTypeReferences.MAP_OF_RECORDS_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.PROFILE_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.USER_STATS_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.STREAK_REF;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ExchangeService exchangeService;
    private final PersonalBestMapper personalBestMapper;
    private final CurrentTestActivityMapper currentTestActivityMapper;
    private final StreakMapper streakMapper;

    public CheckName checkName(String name) {
        URI uri = UriBuilder.fromPath("users/checkName/{name}")
                .build(name);
        return exchangeService.makeGetRequest(uri, CHECK_NAME_REF);
    }

    public Map<String, List<PersonalBestDto>> getPersonalBests(String mode) {
        URI uri = UriBuilder.fromPath("/users/personalBests")
                .queryParam("mode", mode)
                .build();
        Map<String, List<PersonalBest>> response = exchangeService.makeGetRequest(uri, MAP_OF_RECORDS_REF);
        return response.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> personalBestMapper.toDto(entry.getValue())
                ));
    }

    public List<PersonalBestDto> getPersonalBests(String mode, String mode2) {
        URI uri = UriBuilder.fromPath("/users/personalBests")
                .queryParam("mode", mode)
                .queryParam("mode2", mode2)
                .build();
        List<PersonalBest> response = exchangeService.makeGetRequest(uri, LIST_OF_RECORDS);
        return personalBestMapper.toDto(response);
    }

    public UserStats getUserStats() {
        URI uri = UriBuilder.fromPath("/users/stats")
                .build();
        return exchangeService.makeGetRequest(uri, USER_STATS_REF);
    }

    public Profile getProfile(String name) {
        URI uri = UriBuilder.fromPath("/users/{name}/profile")
                .build(name);
        return exchangeService.makeGetRequest(uri, PROFILE_REF);
    }

    public CurrentTestActivityDto getCurrentTestActivity() {
        URI uri = UriBuilder.fromPath("/users/currentTestActivity")
                .build();
        CurrentTestActivity response = exchangeService.makeGetRequest(uri, CURRENT_TEST_ACTIVITY_REF);
        return currentTestActivityMapper.toDto(response);
    }

    public StreakDto getStreak() {
        URI uri = UriBuilder.fromPath("/users/streak")
                .build();
        Streak streak = exchangeService.makeGetRequest(uri, STREAK_REF);
        return streakMapper.ToDto(streak);
    }
}
