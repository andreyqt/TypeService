package holymagic.typeservice.service;

import holymagic.typeservice.dto.CheckNameDto;
import holymagic.typeservice.dto.CurrentTestActivityDto;
import holymagic.typeservice.dto.PersonalBestDto;
import holymagic.typeservice.dto.StreakDto;
import holymagic.typeservice.mapper.CheckNameMapper;
import holymagic.typeservice.mapper.CurrentTestActivityMapper;
import holymagic.typeservice.mapper.PersonalBestMapper;
import holymagic.typeservice.mapper.StreakMapper;
import holymagic.typeservice.model.user.CheckName;
import holymagic.typeservice.model.Response;
import holymagic.typeservice.model.user.CurrentTestActivity;
import holymagic.typeservice.model.user.Profile;
import holymagic.typeservice.model.user.Stats;
import holymagic.typeservice.model.user.Streak;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static holymagic.typeservice.model.ParameterizedTypeReferences.CHECK_NAME_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.CURRENT_TEST_ACTIVITY_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.LIST_OF_RECORDS;
import static holymagic.typeservice.model.ParameterizedTypeReferences.MAP_OF_LIST_OF_RECORDS;
import static holymagic.typeservice.model.ParameterizedTypeReferences.PROFILE_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.STATS_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.STREAK_REF;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestClient restClient;
    private final CheckNameMapper checkNameMapper;
    private final PersonalBestMapper personalBestMapper;
    private final CurrentTestActivityMapper currentTestActivityMapper;
    private final StreakMapper streakMapper;

    public CheckNameDto checkName(String name) {
        URI uri = UriBuilder.fromPath("users/checkName/{name}")
                .build(name);
        Response<CheckName> response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(CHECK_NAME_REF);
        return checkNameMapper.toDto(response);
    }

    public Map<String, List<PersonalBestDto>> getPersonalBests(String mode) {
        URI uri = UriBuilder.fromPath("/users/personalBests")
                .queryParam("mode", mode)
                .build();
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(MAP_OF_LIST_OF_RECORDS)
                .getData()
                .entrySet()
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
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(LIST_OF_RECORDS)
                .getData()
                .stream()
                .map(personalBestMapper::toDto)
                .toList();
    }

    public Stats getStats() {
        URI uri = UriBuilder.fromPath("/users/stats")
                .build();
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(STATS_REF)
                .getData();
    }

    public Profile getProfile(String name) {
        URI uri = UriBuilder.fromPath("/users/{name}/profile")
                .build(name);
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(PROFILE_REF)
                .getData();
    }

    public CurrentTestActivityDto getCurrentTestActivity() {
        URI uri = UriBuilder.fromPath("/users/currentTestActivity")
                .build();
        CurrentTestActivity activity = restClient.get()
                .uri(uri)
                .retrieve()
                .body(CURRENT_TEST_ACTIVITY_REF)
                .getData();
        return currentTestActivityMapper.toDto(activity);
    }

    public StreakDto getStreak() {
        URI uri = UriBuilder.fromPath("/users/streak")
                .build();
        Streak streak = restClient.get()
                .uri(uri)
                .retrieve()
                .body(STREAK_REF)
                .getData();
        return streakMapper.ToDto(streak);
    }
}
