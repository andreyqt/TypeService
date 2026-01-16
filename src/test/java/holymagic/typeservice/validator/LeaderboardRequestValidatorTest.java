package holymagic.typeservice.validator;

import holymagic.typeservice.exception.DataValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LeaderboardRequestValidatorTest {

    private LeaderboardRequestValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new LeaderboardRequestValidator();
        ReflectionTestUtils.setField(validator, "maxPageNumber", 1000);
        ReflectionTestUtils.setField(validator, "maxPageSize", 200);
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 1001, 1002})
    @DisplayName("throw exception for negative and greater than 1000 page number")
    public void validateInvalidPageNumber(int page) {
        assertThrows(DataValidationException.class, () -> validator.validatePage(page));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100, 200, 300, 999})
    @DisplayName("validates page number successfully")
    public void validatePageNumber(int page) {
        assertDoesNotThrow(() -> validator.validatePage(page));
    }

    @Test
    @DisplayName("validates null page number")
    public void validateNullPageNumber() {
        assertDoesNotThrow(() -> validator.validatePage(null));
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 0, 5, 9, 201, 202})
    @DisplayName("throws exception for invalid page size")
    public void validateInvalidPageSize(int pageSize) {
        assertThrows(DataValidationException.class, () -> validator.validatePageSize(pageSize));
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 11, 100, 200})
    @DisplayName("validates page size successfully")
    public void validatePageSize(int pageSize) {
        assertDoesNotThrow(() -> validator.validatePageSize(pageSize));
    }

    @Test
    @DisplayName("validates null pageSize")
    public void validateNullPage() {
        assertDoesNotThrow(() -> validator.validatePageSize(null));
    }


    @ParameterizedTest
    @ValueSource(strings = {"russian", "spanish", "french"})
    @DisplayName("throws exception for not allowed languages for global leaderboards")
    public void validateNotallowedLanguages(String language) {
        assertThrows(DataValidationException.class, () -> validator.ValidateGlobalLeaderboardArgs(language,
                "time", "60", null, null));
    }

    @Test
    @DisplayName("validates language successfully")
    public void validateLanguage() {
        assertDoesNotThrow(() -> validator.ValidateGlobalLeaderboardArgs("english",
                "time", "60", null, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"russian", "dutch", "japanese"})
    @DisplayName("throws exception for not allowed languages for daily leaderboards")
    public void validateNotAllowedLanguagesForDailyLbs(String language) {
        assertThrows(DataValidationException.class, () -> validator.validateDailyLeaderboardArgs(language,
                "time", "60", null, null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"english", "spanish", "german"})
    @DisplayName("validates language successfully")
    public void validateLanguagesForDailyLbs(String language) {
        assertDoesNotThrow(() -> validator.validateDailyLeaderboardArgs(language,
                "time", "60", null, null));
    }

    @ParameterizedTest
    @MethodSource("provideValidLeaderboardArgs")
    @DisplayName("validates all args for global lbs successfully")
    public void validateGlobalLeaderboardArgs(String language, String mode, String mode2,
                                              Integer page, Integer pageSize) {
        assertDoesNotThrow(() -> validator.ValidateGlobalLeaderboardArgs(language, mode, mode2, page, pageSize));
    }

    private static Stream<Arguments> provideValidLeaderboardArgs() {
        return Stream.of(
                Arguments.of("english", "time", "60", 50, null),
                Arguments.of("english", "time", "60", null, null),
                Arguments.of("english", "time", "15", 0, null),
                Arguments.of("english", "time", "15", 1, 10)
        );
    }

}
