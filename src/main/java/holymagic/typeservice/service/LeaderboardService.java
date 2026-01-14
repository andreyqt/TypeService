package holymagic.typeservice.service;

import holymagic.typeservice.dto.RankedRaceDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.mapper.RankedRaceMapper;
import holymagic.typeservice.mapper.WeeklyActivityMapper;
import holymagic.typeservice.model.leaderboard.Leaderboard;
import holymagic.typeservice.model.leaderboard.RankedRace;
import holymagic.typeservice.model.leaderboard.WeeklyActivity;
import holymagic.typeservice.repository.RankedRaceRepository;
import holymagic.typeservice.validator.LeaderboardValidator;
import holymagic.typeservice.validator.PublicDataValidator;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LEADERBOARD_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.RANKED_RACE_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.XP_LEADERBOARD_REF;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final RestClient restClient;
    private final RankedRaceMapper rankedRaceMapper;
    private final PublicDataValidator publicDataValidator;
    private final LeaderboardValidator leaderboardValidator;
    private final WeeklyActivityMapper weeklyActivityMapper;
    private final LeaderboardCache leaderboardCache;
    private final RankedRaceRepository rankedRaceRepository;

    public List<RankedRaceDto> getLeaderboard(String language, String mode, String mode2,
                                              @Nullable Integer page, @Nullable Integer pageSize,
                                              @Nullable Boolean friendsOnly) {

        List<RankedRace> racesFromCache = getRacesFromCache(language, mode, mode2, page, pageSize, friendsOnly);

        if(racesFromCache != null) {
            log.info("retrieved leaderboard from cache");
            return rankedRaceMapper.toDto(racesFromCache);
        }

        URI uri = prepareGetLeaderboardUri("/leaderboards", language, mode, mode2, page, pageSize, friendsOnly);

        Leaderboard leaderboard = restClient.get()
                .uri(uri)
                .retrieve()
                .body(LEADERBOARD_REF)
                .getData();

        String message = String.format(
                "could not get the leaderboard for args: language=%s, mode=%s, mode2=%s, page=%s, pageSize=%s, friendsOnly=%s",
                language, mode, mode2, page, pageSize, friendsOnly
        );
        validateData(leaderboard, message);

        return rankedRaceMapper.toDto(leaderboard.getEntries());
    }

    public RankedRaceDto getRank(String language, String mode, String mode2, @Nullable Boolean friendsOnly) {
        URI uri = prepareGetLeaderboardUri("/leaderboards/rank", language, mode, mode2, null, null, friendsOnly);

        RankedRace rankedRace = restClient.get()
                .uri(uri)
                .retrieve()
                .body(RANKED_RACE_REF)
                .getData();

        String message = String.format(
                "could not get the rank for args: language=%s, mode=%s, mode2=%s, friendsOnly=%s",
                language, mode, mode2, friendsOnly
        );
        validateData(rankedRace, message);

        return rankedRaceMapper.toDto(rankedRace);
    }

    public List<RankedRaceDto> getDailyLeaderboard(String language, String mode, String mode2,
                                                   @Nullable Integer page, @Nullable Integer pageSize,
                                                   @Nullable Boolean friendsOnly) {
        URI uri = prepareGetLeaderboardUri("/leaderboards/daily", language, mode, mode2,
                page, pageSize, friendsOnly);

        Leaderboard leaderboard = restClient.get()
                .uri(uri)
                .retrieve()
                .body(LEADERBOARD_REF)
                .getData();

        String message = String.format(
                "could not get the daily leaderboard for args: language=%s, mode=%s, mode2=%s, page=%s, pageSize=%s, friendsOnly=%s",
                language, mode, mode2, page, pageSize, friendsOnly
        );
        validateData(leaderboard, message);

        return rankedRaceMapper.toDto(leaderboard.getEntries());
    }

    public List<WeeklyActivityDto> getWeeklyXpLeaderboard(@Nullable Boolean friendsOnly,
                                                          @Nullable Integer page, @Nullable Integer pageSize) {
        UriBuilder builder = UriBuilder.fromPath("/leaderboards/xp/weekly");
        URI uri = addOptionalParams(builder, friendsOnly, page, pageSize);

        List<WeeklyActivity> activity = restClient.get()
                .uri(uri)
                .retrieve()
                .body(XP_LEADERBOARD_REF)
                .getData().getEntries();

        validateData(activity, "could not get the weekly xp leaderboard");
        return weeklyActivityMapper.toDto(activity);
    }


    @PostConstruct
    public void initializeLeaderboardCache() {
        List<RankedRace> initialRaces = new ArrayList<>(50);
        URI firstUri = prepareGetLeaderboardUri("/leaderboards", "english", "time",
                "60", 0, 50, null);
        try {
            Leaderboard firstLeaderboard = restClient.get()
                    .uri(firstUri)
                    .retrieve()
                    .body(LEADERBOARD_REF)
                    .getData();
            initialRaces.addAll(firstLeaderboard.getEntries());
            leaderboardCache.update(initialRaces);
            log.info("caches has been initialized successfully");
        } catch (Exception e) {
            log.error("could not initialize the leaderboard cache: {}", e.getMessage());
        }

    }

    public List<RankedRace> getRacesFromCache(String language, String mode, String mode2,
                                               @Nullable Integer page, @Nullable Integer pageSize,
                                               @Nullable Boolean friendsOnly) {
        if (language.equals("english") && mode.equals("time") && mode2.equals("60") && friendsOnly == null
        && page == null && pageSize == null) {
            return leaderboardCache.getAll();
        }
        return null;
    }

    @Async("cacheUpdateExecutor")
    @Scheduled(cron = "${leaderboard_update_cron}")
    public void updateLeaderboardCache() {
        initializeLeaderboardCache();
    }

    private URI prepareGetLeaderboardUri(String path, String language, String mode, String mode2,
                                         @Nullable Integer page, @Nullable Integer pageSize,
                                         @Nullable Boolean friendsOnly) {
        UriBuilder builder = UriBuilder.fromPath(path);
        publicDataValidator.validatePublicDataArgs(language, mode, mode2);
        builder.queryParam("language", language);
        builder.queryParam("mode", mode);
        builder.queryParam("mode2", mode2);
        return addOptionalParams(builder, friendsOnly, page, pageSize);
    }

    private URI addOptionalParams(UriBuilder builder, @Nullable Boolean friendsOnly,
                                  @Nullable Integer page, @Nullable Integer pageSize) {
        if (page != null) {
            leaderboardValidator.validatePageNumber(page);
            builder.queryParam("page", page);
        }
        if (pageSize != null) {
            leaderboardValidator.validatePageSize(pageSize);
            builder.queryParam("pageSize", pageSize);
        }
        if (friendsOnly != null) {
            builder.queryParam("friendsOnly", true);
        }
        return builder.build();
    }

    private void validateData(Object data, String message) {
        if (data == null) {
            throw new NotFoundException(message);
        }
    }

}
