package holymagic.typeservice.service;

import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.mapper.RaceMapper;
import holymagic.typeservice.model.Response;
import holymagic.typeservice.model.race.Race;
import holymagic.typeservice.repository.RaceRepository;
import holymagic.typeservice.validator.RaceValidator;
import jakarta.annotation.Nullable;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LIST_OF_RACES_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.RACE_REF;

@Slf4j
@Service
@RequiredArgsConstructor
public class RaceService {

    private final RaceMapper raceMapper;
    private final RaceValidator raceValidator;
    private final RestClient restClient;
    private final RaceCache raceCache;
    private final RaceRepository raceRepository;

    public List<RaceDto> getResults(@Nullable Long onOrAfterTimestamp,
                                    @Nullable Integer offset,
                                    @Nullable Integer limit) {
        if (onOrAfterTimestamp == null || offset == null || limit == null) {
            List<Race> racesFromCache = raceCache.getAll();
            return raceMapper.toDto(racesFromCache);
        }
        URI uri = prepareGetResultsUri(onOrAfterTimestamp, offset, limit);
        Response<List<Race>> results = restClient.get()
                .uri(uri)
                .retrieve()
                .body(LIST_OF_RACES_REF);
        List<Race> races = results.getData();
        if (races == null) {
            throw new NotFoundException("results not found");
        } else {
            log.info("received {} results", races.size());
            raceCache.add(races);
            return raceMapper.toDto(results.getData());
        }
    }

    public RaceDto getResultById(String id) {
        Race raceFromCache = raceCache.getById(id);
        if (raceFromCache != null) {
            return raceMapper.toDto(raceFromCache);
        }
        URI uri = UriBuilder.fromPath("/results/id/{id}")
                .build(id);
        Race race = restClient.get()
                .uri(uri)
                .retrieve()
                .body(RACE_REF)
                .getData();
        if (race == null || race.getId() == null) {
            throw new NotFoundException("race was not found");
        }
        raceCache.add(race);
        return raceMapper.toDto(race);
    }

    public RaceDto getLastResult() {
        URI uri = UriBuilder.fromPath("/results/last")
                .build();
        Race race = restClient.get()
                .uri(uri)
                .retrieve()
                .body(RACE_REF)
                .getData();
        if (race == null || race.getId() == null) {
            throw new NotFoundException("race was not found");
        }
        return raceMapper.toDto(race);
    }

    @Transactional
    public RaceDto saveRace(String raceId) {
        Race raceFromCache = raceCache.getById(raceId);
        if (raceFromCache != null) {
            raceRepository.save(raceFromCache);
            return raceMapper.toDto(raceFromCache);
        }
        throw new NotFoundException("race is not in cache");
    }

    @Transactional
    public RaceDto saveRace(Long timestamp) {
        Race raceFromCache = raceCache.get(timestamp);
        if (raceFromCache != null) {
            raceRepository.save(raceFromCache);
            return raceMapper.toDto(raceFromCache);
        }
        throw new NotFoundException("race is not in cache");
    }

    @Transactional
    public void deleteRace(String raceId) {
        Optional<Race> race = raceRepository.findById(raceId);
        if (race.isPresent()) {
            raceRepository.delete(race.get());
            log.info("race {} was deleted", raceId);
        } else throw new NotFoundException("couldn't delete by id: race is not in db");
    }

    @Transactional
    public void deleteRace(Long timestamp) {
        Race race = raceRepository.findByTimestamp(timestamp);
        if (race != null) {
            raceRepository.delete(race);
            log.info("race with timestamp {} was deleted", timestamp);
        } else throw new NotFoundException("couldn't delete by timestamp: race is not in db");
    }

    @Transactional
    public void saveAllRacesFromCache(List<Race> racesFromCache) {
        List<Race> races = raceCache.getAll();
        raceRepository.saveAll(racesFromCache);
    }

    @Async("cacheUpdateExecutor")
    @Scheduled(fixedRateString = "${cache_update_interval}m")
    public void updateCache() {
        URI uri = UriBuilder.fromPath("/results")
                .queryParam("limit", "80")
                .build();
        List<Race> races = restClient.get()
                .uri(uri)
                .retrieve()
                .body(LIST_OF_RACES_REF)
                .getData();
        if (races == null) {
            log.warn("wasn't able to get races for cache");
        } else {
            raceCache.add(races);
        }
    }

    @Async("cacheUpdateExecutor")
    @Scheduled(fixedRateString = "${cache_removal_interval}m")
    public void removeOldRaces() {
        raceCache.checkBound();
    }

    private URI prepareGetResultsUri(@Nullable Long onOrAfterTimestamp,
                                     @Nullable Integer offset,
                                     @Nullable Integer limit) {
        UriBuilder builder = UriBuilder.fromPath("/results");
        if (onOrAfterTimestamp != null) {
            raceValidator.validateTimestamp(onOrAfterTimestamp);
            builder.queryParam("onOrAfterTimestamp", onOrAfterTimestamp);
        }
        if (offset != null) {
            raceValidator.validateOffset(offset);
            builder.queryParam("offset", offset);
        }
        if (limit != null) {
            raceValidator.validateLimit(limit);
            builder.queryParam("limit", limit);
        }
        return builder.build();
    }

}
