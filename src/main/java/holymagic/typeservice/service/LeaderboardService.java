package holymagic.typeservice.service;

import holymagic.typeservice.cache.LeaderboardCache;
import holymagic.typeservice.dto.RankedRaceDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.mapper.RankedRaceMapper;
import holymagic.typeservice.mapper.WeeklyActivityMapper;
import holymagic.typeservice.model.leaderboard.Leaderboard;
import holymagic.typeservice.model.leaderboard.RankedRace;
import holymagic.typeservice.model.leaderboard.RankedRace15;
import holymagic.typeservice.model.leaderboard.WeeklyActivity;
import holymagic.typeservice.repository.Leaderboard15Repository;
import holymagic.typeservice.repository.LeaderboardRepository;
import jakarta.annotation.Nullable;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final LeaderboardRepository leaderboardRepository;
    private final Leaderboard15Repository leaderboard15Repository;


    @Value("${leaderboard_batch_size}")
    private int batchSize;

    public List<RankedRaceDto> getLeaderboard(String language, String mode, String mode2,
                                              @Nullable Integer page, @Nullable Integer pageSize,
                                              @Nullable Boolean friendsOnly) {

        if (canGetFromCache(mode2, page, pageSize, friendsOnly)) {
            List<RankedRace> racesFromCache = leaderboardCache.getSome(pageSize);
            if (racesFromCache == null || racesFromCache.isEmpty()) {
                log.warn("couldn't receive lb from cache");
            } else {
                log.info("retrieved lb from cache");
                return rankedRaceMapper.toDto(racesFromCache);
            }
        }

        if (canGetFromDb(mode2, page, pageSize, friendsOnly)) {
            switch (mode2) {
                case "60":
                    List<RankedRace> racesFromDb = leaderboardRepository.findAll();
                    if (racesFromDb.isEmpty()) {
                        log.warn("couldn't receive 60s lb from db");
                    } else {
                        log.info("retrieved 60s lb from db");
                        return rankedRaceMapper.toDto(racesFromDb);
                    }
                    break;
                case"15":
                    List<RankedRace15> shortRacesFromDb = leaderboard15Repository.findAll();
                    if (shortRacesFromDb.isEmpty()) {
                        log.warn("couldn't receive 15s lb from db");
                    } else {
                        log.info("retrieved 15s lb from db");
                        return shortRacesFromDb.stream().map(rankedRaceMapper::toDto).toList();
                    }
                    break;
            }
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

    @Transactional
    public void getAndSave(String mode2) {
        URI uri = prepareGetLeaderboardUri("/leaderboards", "english", "time",
                mode2, 0, batchSize, null);
        List<RankedRace> races = exchangeService.makeGetRequest(uri, LEADERBOARD_REF).getEntries();
        switch (mode2) {
            case "60":
                saveMainLeaderboard(races);
                break;
            case "15":
                saveAdditionalLeaderboard(races);
                break;
        }
    }

    @Async("cacheUpdateExecutor")
    @Scheduled(cron = "${leaderboard_update_cron}")
    public void updateLeaderboardCacheAsync() {
        URI uri = prepareGetLeaderboardUri("/leaderboards", "english", "time",
                "60", 0, leaderboardCache.getCapacity(), null);
        List<RankedRace> races = exchangeService.makeGetRequest(uri, LEADERBOARD_REF).getEntries();
        leaderboardCache.update(races);
    }

    @Transactional
    public void saveMainLeaderboard(List<RankedRace> races) {
        if (leaderboardRepository.hasAnyRecords()) {
            for (RankedRace race : races) {
                RankedRace entity = leaderboardRepository.findByRank(race.getRank());
                rankedRaceMapper.updateEntity(race, entity);
                leaderboardRepository.save(entity);
            }
            log.info("updated 60s lbs");
        } else {
            leaderboardRepository.saveAll(races);
            log.info("saved 60s lbs");
        }
        leaderboardCache.update(races);
    }

    @Transactional
    public void saveAdditionalLeaderboard(List<RankedRace> races) {
        if (leaderboard15Repository.hasAnyRecords()) {
            for (RankedRace race : races) {
                RankedRace15 entity = leaderboard15Repository.findByRank(race.getRank());
                rankedRaceMapper.updateEntity(race, entity);
                leaderboard15Repository.save(entity);
            }
            log.info("updated 15s lbs");
        } else {
            List<RankedRace15> shortRaces = rankedRaceMapper.toRankedRace15(races);
            leaderboard15Repository.saveAll(shortRaces);
            log.info("saved 15s lbs");
        }
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
        if (pageSize != null) {
            builder.queryParam("pageSize", pageSize);
        }
        if (friendsOnly != null) {
            builder.queryParam("friendsOnly", friendsOnly);
        }
        return builder.build();
    }

    private boolean canGetFromCache(String mode2, Integer page, Integer pageSize, Boolean friendsOnly) {
        return mode2.equals("60") && (friendsOnly == null || !friendsOnly) && page == null
                && (pageSize == null || pageSize <= leaderboardCache.getCapacity());
    }

    private boolean canGetFromDb(String mode2, Integer page, Integer pageSize, Boolean friendsOnly) {
        return (mode2.equals("60") || mode2.equals("15")) && page == null
                && (pageSize == null || pageSize <= leaderboardCache.getCapacity());
    }

}
