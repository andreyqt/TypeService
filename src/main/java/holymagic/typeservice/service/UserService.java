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
import holymagic.typeservice.repository.PersonalBestRepository;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private final PersonalBestRepository personalBestRepository;

    public CheckName checkName(String name) {
        URI uri = UriBuilder.fromPath("users/checkName/{name}")
                .build(name);
        return exchangeService.makeGetRequest(uri, CHECK_NAME_REF);
    }

    public List<PersonalBestDto> getPersonalBests(String mode, String mode2, String language) {
        URI uri = UriBuilder.fromPath("/users/personalBests")
                .queryParam("mode", mode)
                .queryParam("mode2", mode2)
                .build();
        return exchangeService.makeGetRequest(uri, LIST_OF_RECORDS)
                .stream()
                .map(pb -> {
                            PersonalBest newPb = processPersonalBest(pb, mode, mode2);
                            return personalBestMapper.toDto(newPb);
                        }
                )
                .filter(pb -> pb.getLanguage().contains(language))
                .toList();
    }

    public List<PersonalBest> getAllPersonalBests(String language) {
        URI timeModeUri = UriBuilder.fromPath("/users/personalBests")
                .queryParam("mode", "time")
                .build();
        Map<String, List<PersonalBest>> timePbs = exchangeService.makeGetRequest(timeModeUri, MAP_OF_RECORDS_REF);

        URI wordsModeUri = UriBuilder.fromPath("/users/personalBests")
                .queryParam("mode", "words")
                .build();
        Map<String, List<PersonalBest>> wordsPbs = exchangeService.makeGetRequest(wordsModeUri, MAP_OF_RECORDS_REF);

        List<PersonalBest> allPbs = new ArrayList<>(timePbs.size() + wordsPbs.size());

        timePbs.forEach((k, v) -> {
            v.stream().map(pb -> processPersonalBest(pb, "time", k))
                    .filter(pb -> pb.getLanguage().contains(language))
                    .forEach(allPbs::add);
        });
        wordsPbs.forEach((k, v) -> {
            v.stream().map(pb -> processPersonalBest(pb, "words", k))
                    .filter(pb -> pb.getLanguage().contains(language))
                    .forEach(allPbs::add);
        });

        return allPbs;
    }

    public List<PersonalBestDto> getAllPersonalBestDtos(String language) {
        List<PersonalBest> allPbs = getAllPersonalBests(language);
        return personalBestMapper.toDto(allPbs);
    }

    private PersonalBest processPersonalBest(PersonalBest personalBest,
                                             String mode, String mode2) {
        personalBest.setMode(mode);
        personalBest.setMode2(mode2);
        if (personalBest.getNumbers() == null) {
            personalBest.setNumbers(false);
        }
        if (personalBest.getPunctuation() == null) {
            personalBest.setPunctuation(false);
        }
        return personalBest;
    }

    @Transactional
    public void savePersonalBests(String language) {
        List<PersonalBest> pbs = getAllPersonalBests(language);
        for (PersonalBest personalBest : pbs) {
            personalBestRepository.upsert(personalBest);
        }
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
