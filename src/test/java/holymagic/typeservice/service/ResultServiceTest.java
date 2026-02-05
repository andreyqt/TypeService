package holymagic.typeservice.service;

import holymagic.typeservice.MyTestUtils;
import holymagic.typeservice.dto.ResultDto;
import holymagic.typeservice.mapper.ResultMapperImpl;
import holymagic.typeservice.model.result.Result;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LIST_OF_RESULTS_REF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResultServiceTest {

    @InjectMocks
    private ResultService resultService;

    @Mock
    private ExchangeService exchangeService;
    @Spy
    private ResultMapperImpl resultMapper;

    List<Result> results;
    List<ResultDto> expectedResults;
    ArgumentCaptor<URI> uriCaptor;

    @BeforeEach
    public void setUp() {
        results = ResultServiceTestData.provideResults();
        expectedResults = ResultServiceTestData.provideResultDtos(results);
        uriCaptor = ArgumentCaptor.forClass(URI.class);
    }

    @Test
    public void getResultsTest() {
        URI expectedUri = URI.create("/results?onOrAfterTimestamp=1664536400000&limit=40");
        when(exchangeService.makeGetRequest(any(URI.class), eq(LIST_OF_RESULTS_REF))).thenReturn(results);
        List<ResultDto> actualResults = resultService.getResults(1664536400000L, 40);
        MyTestUtils.verifyExchange(expectedUri, LIST_OF_RESULTS_REF, exchangeService, uriCaptor);
        assertEquals(expectedResults, actualResults);
    }

}
