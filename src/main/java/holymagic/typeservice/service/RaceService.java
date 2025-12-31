package holymagic.typeservice.service;

import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.mapper.RaceMapper;
import holymagic.typeservice.model.Response;
import holymagic.typeservice.model.race.Race;
import holymagic.typeservice.validator.RaceValidator;
import jakarta.annotation.Nullable;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LIST_OF_RACES_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.RACE_REF;

@Slf4j
@Service
@RequiredArgsConstructor
public class RaceService {
    private final RaceMapper raceMapper;
    private final RaceValidator raceValidator;
    private final RestClient restClient;

    public List<RaceDto> getResults(@Nullable Long onOrAfterTimestamp,
                                    @Nullable Integer offset,
                                    @Nullable Integer limit) {
        URI uri = prepareGetResultsUri(onOrAfterTimestamp, offset, limit);
        Response<List<Race>> results = restClient.get()
                .uri(uri)
                .retrieve()
                .body(LIST_OF_RACES_REF);
        if (results.getData() == null) {
            throw new NotFoundException("results not found");
        } else {
            log.info("received {} results", results.getData().size());
            return raceMapper.toDto(results.getData());
        }
    }

    public RaceDto getResultById(String id) {
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

    private URI prepareGetResultsUri(Long onOrAfterTimestamp, Integer offset, Integer limit) {
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
