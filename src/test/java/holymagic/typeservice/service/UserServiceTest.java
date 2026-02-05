package holymagic.typeservice.service;

import holymagic.typeservice.MyTestUtils;
import holymagic.typeservice.dto.CurrentTestActivityDto;
import holymagic.typeservice.dto.PersonalBestDto;
import holymagic.typeservice.dto.StreakDto;
import holymagic.typeservice.mapper.CurrentTestActivityMapper;
import holymagic.typeservice.mapper.PersonalBestMapperImpl;
import holymagic.typeservice.mapper.StreakMapper;
import holymagic.typeservice.model.user.PersonalBest;
import holymagic.typeservice.model.user.CheckName;
import holymagic.typeservice.model.user.CurrentTestActivity;
import holymagic.typeservice.model.user.Profile;
import holymagic.typeservice.model.user.Streak;
import holymagic.typeservice.model.user.UserStats;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static holymagic.typeservice.model.ParameterizedTypeReferences.CHECK_NAME_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.CURRENT_TEST_ACTIVITY_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.LIST_OF_RECORDS;
import static holymagic.typeservice.model.ParameterizedTypeReferences.MAP_OF_RECORDS_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.PROFILE_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.STREAK_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.USER_STATS_REF;
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
        MyTestUtils.verifyExchange(expectedUri, CHECK_NAME_REF, exchangeService, uriCaptor);
    }

    @Test
    public void getPersonalBestTest() {
        List<PersonalBest> pbs = UserServiceTestData.generatePersonalBests();
        URI expectedUri = URI.create("/users/personalBests?mode=time&mode2=60");
        when(exchangeService.makeGetRequest(any(URI.class), eq(LIST_OF_RECORDS))).thenReturn(pbs);
        List<PersonalBestDto> result = userService.getPersonalBests("time", "60", "english");
        MyTestUtils.verifyExchange(expectedUri, LIST_OF_RECORDS, exchangeService, uriCaptor);
        assertEquals(5, result.size());
    }

    @Test
    public void getAllPersonalBestsTest() {
        Map<String, List<PersonalBest>> timePbs = UserServiceTestData.provideMapOfPersonalBest();
        Map<String, List<PersonalBest>> wordsPbs = UserServiceTestData.provideMapOfPersonalBest();
        URI expectedUriForTimeMode = URI.create("/users/personalBests?mode=time");
        URI expectedUriForWordsMode = URI.create("/users/personalBests?mode=words");
        when(exchangeService.makeGetRequest(eq(expectedUriForTimeMode), eq(MAP_OF_RECORDS_REF))).thenReturn(timePbs);
        when(exchangeService.makeGetRequest(eq(expectedUriForWordsMode), eq(MAP_OF_RECORDS_REF))).thenReturn(wordsPbs);
        List<PersonalBestDto> result = userService.getAllPersonalBestDtos("english");
        verify(exchangeService, times(2)).makeGetRequest(any(URI.class), eq(MAP_OF_RECORDS_REF));
        assertEquals(40, result.size());
    }

    @Test
    public void getUserStatsTest() {
        URI expectedUri = URI.create("/users/stats");
        UserStats stats = UserServiceTestData.provideUserStats();
        when(exchangeService.makeGetRequest(any(URI.class), eq(USER_STATS_REF))).thenReturn(stats);
        UserStats actualStats = userService.getUserStats();
        assertEquals(stats, actualStats);
        MyTestUtils.verifyExchange(expectedUri, USER_STATS_REF, exchangeService, uriCaptor);
    }

    @Test
    public void getProfileTest() {
        URI expectedUri = URI.create("/users/test_name_123/profile");
        Profile profile = UserServiceTestData.provideProfile();
        when(exchangeService.makeGetRequest(any(URI.class), eq(PROFILE_REF))).thenReturn(profile);
        Profile actualProfile = userService.getProfile("test_name_123");
        assertEquals(profile, actualProfile);
        MyTestUtils.verifyExchange(expectedUri, PROFILE_REF, exchangeService, uriCaptor);
    }

    @Test
    public void getCurrentTestActivityTest() {
        URI expectedUri = URI.create("/users/currentTestActivity");
        CurrentTestActivity activity = UserServiceTestData.provideCurrentTestActivity();
        when(exchangeService.makeGetRequest(any(URI.class), eq(CURRENT_TEST_ACTIVITY_REF))).thenReturn(activity);
        CurrentTestActivityDto expectedActivity = currentTestActivityMapper.toDto(activity);
        CurrentTestActivityDto actualActivity = userService.getCurrentTestActivity();
        assertEquals(expectedActivity, actualActivity);
        MyTestUtils.verifyExchange(expectedUri, CURRENT_TEST_ACTIVITY_REF, exchangeService, uriCaptor);
    }

    @Test
    public void getStreakTest() {
        URI expectedUri = URI.create("/users/streak");
        Streak streak = UserServiceTestData.provideStreak();
        when(exchangeService.makeGetRequest(any(URI.class), eq(STREAK_REF))).thenReturn(streak);
        StreakDto actualStreak = userService.getStreak();
        StreakDto expectedStreak = streakMapper.ToDto(streak);
        assertEquals(expectedStreak, actualStreak);
        MyTestUtils.verifyExchange(expectedUri, STREAK_REF, exchangeService, uriCaptor);
    }

}
