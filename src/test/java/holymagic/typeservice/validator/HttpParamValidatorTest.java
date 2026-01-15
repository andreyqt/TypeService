package holymagic.typeservice.validator;

import holymagic.typeservice.exception.DataValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class HttpParamValidatorTest {

    private HttpParamValidator httpParamValidator;

    @BeforeEach
    public void setUp() {
        httpParamValidator = new HttpParamValidator();
        ReflectionTestUtils.setField(httpParamValidator, "maxPageNumber", 1000);
        ReflectionTestUtils.setField(httpParamValidator, "maxPageSize", 200);
        ReflectionTestUtils.setField(httpParamValidator, "initialTimestamp", 1589428800000L);
        ReflectionTestUtils.setField(httpParamValidator, "maxLimit", 1000);
    }

    @ParameterizedTest
    @ValueSource(longs = {1589428700000L, 1589428600000L, 1589428500000L})
    @DisplayName("throws exception for old timestamps")
    public void validateOldTimestamp(long timestamp) {
        assertThrows(DataValidationException.class, () -> httpParamValidator.validateTimestamp(timestamp));
    }

    @ParameterizedTest
    @MethodSource("provideFutureTimestamps")
    @DisplayName("throws exception for future timestamps")
    public void validateFutureTimestamp(long timestamp) {
        assertThrows(DataValidationException.class, () -> httpParamValidator.validateTimestamp(timestamp));
    }

    @ParameterizedTest
    @ValueSource(longs = {1689428800000L, 1689428600000L, 1689428500000L})
    @DisplayName("validates timestamps successfully")
    public void validateTimestamps(long timestamp) {
        assertDoesNotThrow(() -> httpParamValidator.validateTimestamp(timestamp));
    }

    @ParameterizedTest
    @ValueSource(ints = {-3, -2, -1, 1000, 1001, 1002})
    @DisplayName("throws exception for negative offset and greater than maxLimit")
    public void validateInvalidOffset(int offset) {
        assertThrows(DataValidationException.class, () -> httpParamValidator.validateOffset(offset));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100, 200, 300, 999})
    @DisplayName("validates offset successfully")
    public void validateOffset(int offset) {
        assertDoesNotThrow(() -> httpParamValidator.validateOffset(offset));
    }

    @ParameterizedTest
    @ValueSource(ints = {-3, -2, -1, 0, 1001, 1002, 1003})
    @DisplayName("throws exception for negative/zero limit and greater than 1000")
    public void validateInvalidLimit(int limit) {
        assertThrows(DataValidationException.class, () -> httpParamValidator.validateLimit(limit));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 200, 300, 999})
    @DisplayName("validates limit successfully")
    public void validateLimit(int limit) {
        assertDoesNotThrow(() -> httpParamValidator.validateLimit(limit));
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 1001, 1002})
    @DisplayName("throw exception for negative and greater than 1000 page number")
    public void validateInvalidPageNumber(int pageNumber) {
        assertThrows(DataValidationException.class, () -> httpParamValidator.validatePageNumber(pageNumber));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 10, 100, 200, 300, 999})
    @DisplayName("valides page number successfully")
    public void validatePageNumber(int pageNumber) {
        assertDoesNotThrow(() -> httpParamValidator.validatePageNumber(pageNumber));
    }

    @ParameterizedTest
    @ValueSource(ints = {-2, -1, 0, 201, 202})
    @DisplayName("throws exception for invalid page size")
    public void validateInvalidPageSize(int pageSize) {
        assertThrows(DataValidationException.class, () -> httpParamValidator.validatePageSize(pageSize));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 200})
    @DisplayName("validates page size successfully")
    public void validatePageSize(int pageSize) {
        assertDoesNotThrow(() -> httpParamValidator.validatePageSize(pageSize));
    }

    @ParameterizedTest
    @MethodSource("invalidLanguages")
    @DisplayName("throws exception for not allowed languages")
    public void validateInvalidLanguage(String language) {
        assertThrows(DataValidationException.class, () -> httpParamValidator.validatePublicDataArgs(language,
                "time", "60"));
    }

    @ParameterizedTest
    @MethodSource("validLanguages")
    @DisplayName("validates languages successfully")
    public void validateLanguage(String language) {
        assertDoesNotThrow(()-> httpParamValidator.validatePublicDataArgs(language, "time", "60"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidModeCombinations")
    @DisplayName("throws exception for invalid mode combinations")
    public void validateInvalidModeCombinations(String mode, String mode2) {
        assertThrows(DataValidationException.class, () -> httpParamValidator.validatePublicDataArgs("english",
                mode, mode2));
    }

    @ParameterizedTest
    @MethodSource("provideValidModeCombinations")
    @DisplayName("validates mode combinations successfully")
    public void validateModeCombinations(String mode, String mode2) {
        assertDoesNotThrow(()-> httpParamValidator.validatePublicDataArgs("english", mode, mode2));
    }

    private static List<String> validLanguages() {
        return List.of("english", "english_1k", "russian", "russian_1k");
    }

    private static List<String> invalidLanguages() {
        return List.of("french", "spanish", "german", "italian");
    }

    private static Stream<Arguments> provideValidModeCombinations() {
        return Stream.of(
                Arguments.of("time", "15"),
                Arguments.of("time", "30"),
                Arguments.of("time", "60"),
                Arguments.of("time", "120"),
                Arguments.of("words", "10"),
                Arguments.of("words", "25"),
                Arguments.of("words", "50"),
                Arguments.of("words", "100")
        );
    }

    private static Stream<Arguments> provideInvalidModeCombinations() {
        return Stream.of(
                Arguments.of("zen", "15"),
                Arguments.of("special", "random"),
                Arguments.of("quote", "100"),
                Arguments.of("multiplayer", "team")
        );
    }

    private static List<Long> provideFutureTimestamps() {
        long currentTimestamp = System.currentTimeMillis();
        return List.of(currentTimestamp + 1 * 86400, currentTimestamp + 2 * 86400, currentTimestamp + 3 * 86400);
    }
}
