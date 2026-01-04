package holymagic.typeservice.service;

import holymagic.typeservice.dto.CheckNameDto;
import holymagic.typeservice.dto.PersonalBestDto;
import holymagic.typeservice.mapper.CheckNameMapper;
import holymagic.typeservice.mapper.CheckNameMapperImpl;
import holymagic.typeservice.mapper.PersonalBestMapper;
import holymagic.typeservice.mapper.PersonalBestMapperImpl;
import holymagic.typeservice.model.user.Stats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static holymagic.typeservice.model.ParameterizedTypeReferences.CHECK_NAME_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.LIST_OF_RECORDS;
import static holymagic.typeservice.model.ParameterizedTypeReferences.MAP_OF_LIST_OF_RECORDS;
import static holymagic.typeservice.model.ParameterizedTypeReferences.STATS_REF;
import static holymagic.typeservice.service.UserServiceTestData.CHECK_NAME_RESPONSE;
import static holymagic.typeservice.service.UserServiceTestData.EXPECTED_CHECK_NAME_DTO;
import static holymagic.typeservice.service.UserServiceTestData.EXPECTED_CHECK_NAME_URI;
import static holymagic.typeservice.service.UserServiceTestData.EXPECTED_DTO_MAP_OF_LIST_OF_RECORDS;
import static holymagic.typeservice.service.UserServiceTestData.EXPECTED_DTO_RECORDS_FOR_30S;
import static holymagic.typeservice.service.UserServiceTestData.EXPECTED_GET_PERSONAL_BEST_30S_URI;
import static holymagic.typeservice.service.UserServiceTestData.EXPECTED_GET_PERSONAL_BEST_URI;
import static holymagic.typeservice.service.UserServiceTestData.EXPECTED_GET_STATS_URI;
import static holymagic.typeservice.service.UserServiceTestData.LIST_OF_RECORDS_RESPONSE_FOR_30S;
import static holymagic.typeservice.service.UserServiceTestData.MAP_OF_LIST_OF_RECORDS_RESPONSE;
import static holymagic.typeservice.service.UserServiceTestData.NAME_TO_CHECK;
import static holymagic.typeservice.service.UserServiceTestData.STATS_RESPONSE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private RestClient restClient;
    @Spy
    private CheckNameMapper checkNameMapper = new CheckNameMapperImpl();
    @Spy
    private PersonalBestMapper personalBestMapper = new PersonalBestMapperImpl();
    @InjectMocks
    private UserService userService;
    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private RestClient.ResponseSpec responseSpec;
    private ArgumentCaptor<URI> uriCaptor;

    @BeforeEach
    public void setUp() {
        uriCaptor = ArgumentCaptor.forClass(URI.class);
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(URI.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    public void checkNameTest() {
        when(responseSpec.body(CHECK_NAME_REF)).thenReturn(CHECK_NAME_RESPONSE);
        CheckNameDto actualCheckNameDto = userService.checkName(NAME_TO_CHECK);
        assertEquals(EXPECTED_CHECK_NAME_DTO, actualCheckNameDto);
        verifyRestClientActions(CHECK_NAME_REF, EXPECTED_CHECK_NAME_URI);
    }

    @Test
    public void getPersonalTest() {
        when(responseSpec.body(MAP_OF_LIST_OF_RECORDS)).thenReturn(MAP_OF_LIST_OF_RECORDS_RESPONSE);
        Map<String, List<PersonalBestDto>> actualDtoMap = userService.getPersonalBests("time");
        assertEquals(EXPECTED_DTO_MAP_OF_LIST_OF_RECORDS, actualDtoMap);
        verifyRestClientActions(MAP_OF_LIST_OF_RECORDS, EXPECTED_GET_PERSONAL_BEST_URI);
    }

    @Test
    public void getPersonalBestForTimeTest() {
        when(responseSpec.body(LIST_OF_RECORDS)).thenReturn(LIST_OF_RECORDS_RESPONSE_FOR_30S);
        List<PersonalBestDto> actualDtoList = userService.getPersonalBests("time", "30");
        verifyRestClientActions(LIST_OF_RECORDS, EXPECTED_GET_PERSONAL_BEST_30S_URI);
        assertEquals(EXPECTED_DTO_RECORDS_FOR_30S, actualDtoList);
    }

    @Test
    public void getStatsTest() {
        when(responseSpec.body(STATS_REF)).thenReturn(STATS_RESPONSE);
        Stats actualStats = userService.getStats();
        assertEquals(STATS_RESPONSE.getData(), actualStats);
        verifyRestClientActions(STATS_REF, EXPECTED_GET_STATS_URI);
    }

    public void verifyRestClientActions(ParameterizedTypeReference reference, URI expectedUri) {
        verify(restClient).get();
        verify(requestHeadersUriSpec).uri(uriCaptor.capture());
        verify(requestHeadersSpec).retrieve();
        verify(responseSpec).body(reference);
        URI capturedUri = uriCaptor.getValue();
        assertEquals(expectedUri, capturedUri);
    }
}
