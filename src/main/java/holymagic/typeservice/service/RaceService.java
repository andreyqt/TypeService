package holymagic.typeservice.service;

import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.mapper.RaceMapper;
import holymagic.typeservice.model.race.Race;
import holymagic.typeservice.repository.RaceRepository;
import holymagic.typeservice.validator.HttpParamValidator;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final RaceCache raceCache;
    private final RaceRepository raceRepository;
    private final ExchangeService exchangeService;
    private final HttpParamValidator httpParamValidator;

    @Value("${default_limit}")
    private int defaultLimit;

    public List<RaceDto> getResultsFromRequest(@Nullable Long onOrAfterTimestamp,
                                               @Nullable Integer offset,
                                               @Nullable Integer limit) {
        URI uri = createGetResulstUri(onOrAfterTimestamp, offset, limit);
        List<Race> races = exchangeService.makeGetRequest(uri, LIST_OF_RACES_REF);
        return raceMapper.toDto(races);
    }

    public RaceDto getResultByIdFromRequest(String id) {
        URI uri = UriBuilder.fromPath("/results/id/{id}")
                .build(id);
        Race race = exchangeService.makeGetRequest(uri, RACE_REF);
        raceCache.add(race);
        return raceMapper.toDto(race);
    }

    public RaceDto getLastResultFromRequest() {
        URI uri = UriBuilder.fromPath("/results/last").build();
        Race race = exchangeService.makeGetRequest(uri, RACE_REF);
        raceCache.add(race);
        return raceMapper.toDto(race);
    }

    public RaceDto getRaceByTimestampFromCache(Long timestamp) {
        Race race = raceCache.get(timestamp);
        if (race != null) {
            return raceMapper.toDto(race);
        }
        throw new EntityNotFoundException(String.format("race with timestamp %s was not found in cache", timestamp));
    }

    @Transactional(readOnly = true)
    public RaceDto getRaceByIdFromDb(String raceId) {
        Optional<Race> race = raceRepository.findById(raceId);
        if (race.isPresent()) {
            return raceMapper.toDto(race.get());
        }
        throw new EntityNotFoundException(String.format("race with id %s was not found", raceId));
    }

    @Transactional(readOnly = true)
    public RaceDto getRaceByTimestampFromDb(Long timestamp) {
        Race race = raceRepository.findByTimestamp(timestamp);
        if (race != null) {
            return raceMapper.toDto(race);
        }
        throw new EntityNotFoundException(String.format("race with timestamp %s was not found", timestamp));
    }

    @Transactional
    public RaceDto saveRace(String raceId) {
        Race raceFromCache = raceCache.getById(raceId);
        if (raceFromCache != null) {
            raceRepository.save(raceFromCache);
            return raceMapper.toDto(raceFromCache);
        }
        throw new EntityNotFoundException("race is not in cache");
    }

    @Transactional
    public RaceDto saveRace(Long timestamp) {
        Race raceFromCache = raceCache.get(timestamp);
        if (raceFromCache != null) {
            raceRepository.save(raceFromCache);
            return raceMapper.toDto(raceFromCache);
        }
        throw new EntityNotFoundException("race is not in cache");
    }

    @Transactional
    public void deleteRace(String raceId) {
        Optional<Race> race = raceRepository.findById(raceId);
        if (race.isPresent()) {
            raceRepository.delete(race.get());
            log.info("race {} was deleted", raceId);
        } else throw new EntityNotFoundException("couldn't delete by id: race is not in db");
    }

    @Transactional
    public void deleteRace(Long timestamp) {
        Race race = raceRepository.findByTimestamp(timestamp);
        if (race != null) {
            raceRepository.delete(race);
            log.info("race with timestamp {} was deleted", timestamp);
        } else throw new EntityNotFoundException("couldn't delete by timestamp: race is not in db");
    }

    @Transactional
    public void saveAllRacesFromCache() {
        List<Race> races = raceCache.getAll();
        raceRepository.saveAll(races);
        log.info("{} races were saved to db", races.size());
    }

    @Async("cacheUpdateExecutor")
    @Scheduled(fixedRateString = "${cache_update_interval}m")
    public void updateCache() {
        int size = raceCache.getSize();
        URI uri = UriBuilder.fromPath("/results")
                .queryParam("limit", size)
                .build();
        List<Race> races = exchangeService.makeGetRequest(uri, LIST_OF_RACES_REF);
        raceCache.update(races);
    }

    @Async("cacheUpdateExecutor")
    @Scheduled(fixedRateString = "${cache_removal_interval}m")
    public void removeOldRaces() {
        raceCache.removeOldRaces();
    }

    private URI createGetResulstUri(Long timestamp, Integer offset, Integer limit) {
        UriBuilder builder = UriBuilder.fromPath("/results");
        if (timestamp != null) {
            httpParamValidator.validateTimestamp(timestamp);
            builder.queryParam("onOrAfterTimestamp", timestamp);
        }
        if (offset != null) {
            httpParamValidator.validateOffset(offset);
            builder.queryParam("offset", offset);
        }
        if (limit == null) {
            builder.queryParam("limit", defaultLimit);
        } else {
            httpParamValidator.validateLimit(limit);
            builder.queryParam("limit", limit);
        }
        return builder.build();
    }

}
