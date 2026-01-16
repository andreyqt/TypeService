package holymagic.typeservice.service;

import holymagic.typeservice.cache.LeaderboardCache;
import holymagic.typeservice.dto.RankedRaceDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.mapper.RankedRaceMapper;
import holymagic.typeservice.mapper.WeeklyActivityMapper;
import holymagic.typeservice.model.leaderboard.Leaderboard;
import holymagic.typeservice.model.leaderboard.RankedRace;
import holymagic.typeservice.model.leaderboard.WeeklyActivity;
import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LEADERBOARD_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.RANKED_RACE_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.XP_LEADERBOARD_REF;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final RankedRaceMapper rankedRaceMapper;
    private final WeeklyActivityMapper weeklyActivityMapper;
    private final LeaderboardCache leaderboardCache;
    private final ExchangeService exchangeService;

    public List<RankedRaceDto> getLeaderboard(String language, String mode, String mode2,
                                              @Nullable Integer page, @Nullable Integer pageSize,
                                              @Nullable Boolean friendsOnly) {

        if (canGetFromCache(mode2, page, pageSize, friendsOnly)) {
            List<RankedRace> racesFromCache = leaderboardCache.getSome(pageSize);
            log.info("retrieved leaderboard from cache");
            return rankedRaceMapper.toDto(racesFromCache);
        }

        URI uri = prepareGetLeaderboardUri("/leaderboards", language, mode, mode2, page, pageSize, friendsOnly);
        Leaderboard leaderboard = exchangeService.makeGetRequest(uri, LEADERBOARD_REF);
        return rankedRaceMapper.toDto(leaderboard.getEntries());
    }

    public RankedRaceDto getRank(String language, String mode, String mode2, @Nullable Boolean friendsOnly) {
        URI uri = prepareGetLeaderboardUri("/leaderboards/rank", language, mode, mode2,
                null, null, friendsOnly);
        RankedRace rankedRace = exchangeService.makeGetRequest(uri, RANKED_RACE_REF);
        return rankedRaceMapper.toDto(rankedRace);
    }

    public List<RankedRaceDto> getDailyLeaderboard(String language, String mode, String mode2,
                                                   @Nullable Integer page, @Nullable Integer pageSize,
                                                   @Nullable Boolean friendsOnly) {
        URI uri = prepareGetLeaderboardUri("/leaderboards/daily", language, mode, mode2,
                page, pageSize, friendsOnly);
        Leaderboard leaderboard = exchangeService.makeGetRequest(uri, LEADERBOARD_REF);
        return rankedRaceMapper.toDto(leaderboard.getEntries());
    }

    public List<WeeklyActivityDto> getWeeklyXpLeaderboard(@Nullable Boolean friendsOnly,
                                                          @Nullable Integer page, @Nullable Integer pageSize) {
        UriBuilder builder = UriBuilder.fromPath("/leaderboards/xp/weekly");
        URI uri = addOptionalParams(builder, friendsOnly, page, pageSize);
        List<WeeklyActivity> activity = exchangeService.makeGetRequest(uri, XP_LEADERBOARD_REF).getEntries();
        return weeklyActivityMapper.toDto(activity);
    }

    @PostConstruct
    public void updateLeaderboardCache() {
        URI uri = prepareGetLeaderboardUri("/leaderboards", "english", "time",
                "60", 0, leaderboardCache.getCapacity(), null);
        List<RankedRace> leaderboard = exchangeService.makeGetRequest(uri, LEADERBOARD_REF)
                .getEntries();
        leaderboardCache.update(leaderboard);
    }

    @Async("cacheUpdateExecutor")
    @Scheduled(cron = "${leaderboard_update_cron}")
    public void updateLeaderboardCacheAsync() {
        updateLeaderboardCache();
    }

    private URI prepareGetLeaderboardUri(String path, String language, String mode, String mode2,
                                         @Nullable Integer page, @Nullable Integer pageSize,
                                         @Nullable Boolean friendsOnly) {
        UriBuilder builder = UriBuilder.fromPath(path);
        builder.queryParam("language", language);
        builder.queryParam("mode", mode);
        builder.queryParam("mode2", mode2);
        return addOptionalParams(builder, friendsOnly, page, pageSize);
    }

    private URI addOptionalParams(UriBuilder builder, @Nullable Boolean friendsOnly,
                                  @Nullable Integer page, @Nullable Integer pageSize) {
        if (page != null) {
            builder.queryParam("page", page);
        }
        builder.queryParam("pageSize", pageSize != null ? pageSize : leaderboardCache.getCapacity());
        if (friendsOnly != null) {
            builder.queryParam("friendsOnly", friendsOnly);
        }
        return builder.build();
    }

    private boolean canGetFromCache(String mode2, Integer page, Integer pageSize, Boolean friendsOnly) {
        if (mode2.equals("60") && (friendsOnly == null || !friendsOnly) && page == null
        && (pageSize == null || pageSize <= leaderboardCache.getCapacity())) {
            return true;
        }
        return false;
    }

}
