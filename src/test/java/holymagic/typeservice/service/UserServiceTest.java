package holymagic.typeservice.service;

import holymagic.typeservice.mapper.CurrentTestActivityMapper;
import holymagic.typeservice.mapper.PersonalBestMapperImpl;
import holymagic.typeservice.mapper.StreakMapper;
import holymagic.typeservice.model.user.CheckName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;

import java.net.URI;

import static holymagic.typeservice.model.ParameterizedTypeReferences.CHECK_NAME_REF;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private ExchangeService exchangeService;
    @Spy
    private PersonalBestMapperImpl personalBestMapper;
    @Mock
    private CurrentTestActivityMapper currentTestActivityMapper;
    @Mock
    private StreakMapper streakMapper;

    ArgumentCaptor<URI> uriCaptor = ArgumentCaptor.forClass(URI.class);

    @Test
    public void checkNameTest() {
        URI expectedUri = URI.create("users/checkName/test_name");
        CheckName checkName = new CheckName(false);
        when(exchangeService.makeGetRequest(any(URI.class), eq(CHECK_NAME_REF))).thenReturn(checkName);
        CheckName result = userService.checkName("test_name");
        assertEquals(checkName, result);
        verifyExchange(expectedUri, CHECK_NAME_REF);
    }

    @Test
    public void getPersonalBestsTest() {
        URI expectedUri = URI.create("/users/personalBests?mode=time");

    }


    public void verifyExchange(URI expectedUri, ParameterizedTypeReference reference) {
        verify(exchangeService, times(1)).makeGetRequest(uriCaptor.capture(), eq(reference));
        assertEquals(expectedUri, uriCaptor.getValue());
    }
}
