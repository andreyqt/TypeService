package holymagic.typeservice.service;

import holymagic.typeservice.dto.ResultDto;
import holymagic.typeservice.mapper.ResultMapper;
import holymagic.typeservice.model.result.Result;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LIST_OF_RESULTS_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.RESULT_REF;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultMapper resultMapper;
    private final ExchangeService exchangeService;

    public List<ResultDto> getResults(Long onOrAfterTimestamp, Integer limit) {
        URI uri = UriBuilder.fromPath("/results")
                .queryParam("onOrAfterTimestamp", onOrAfterTimestamp)
                .queryParam("limit", limit)
                .build();
        List<Result> response = exchangeService.makeGetRequest(uri, LIST_OF_RESULTS_REF);
        log.info("received {} results", response.size());
        return resultMapper.toDto(response);
    }

    public ResultDto getResultById(String id) {
        URI uri = UriBuilder.fromPath("/results/id/{id}").build(id);
        Result response = exchangeService.makeGetRequest(uri, RESULT_REF);
        return resultMapper.toDto(response);
    }

    public ResultDto getLastResult() {
        URI uri = UriBuilder.fromPath("/results/last").build();
        Result response = exchangeService.makeGetRequest(uri, RESULT_REF);
        return resultMapper.toDto(response);
    }

}
