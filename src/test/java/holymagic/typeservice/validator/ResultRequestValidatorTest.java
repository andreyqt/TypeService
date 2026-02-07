package holymagic.typeservice.validator;

import holymagic.typeservice.exception.ParamValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ResultRequestValidatorTest {

    private ResultRequestValidator resultRequestValidator;

    @BeforeEach
    public void setUp() {
        resultRequestValidator = new ResultRequestValidator();
        ReflectionTestUtils.setField(resultRequestValidator, "initialTimestamp", 1589428800000L);
        ReflectionTestUtils.setField(resultRequestValidator, "maxLimit", 1000);
    }

    @ParameterizedTest
    @ValueSource(longs = {1589428700000L, 1589428600000L, 1589428500000L})
    @DisplayName("throws exception for old timestamps")
    public void validateOldTimestamp(long timestamp) {
        assertThrows(ParamValidationException.class, () -> resultRequestValidator.validateTimestamp(timestamp));
    }

    @ParameterizedTest
    @MethodSource("provideFutureTimestamps")
    @DisplayName("throws exception for future timestamps")
    public void validateFutureTimestamp(long timestamp) {
        assertThrows(ParamValidationException.class, () -> resultRequestValidator.validateTimestamp(timestamp));
    }

    @ParameterizedTest
    @ValueSource(longs = {1689428800000L, 1689428600000L, 1689428500000L})
    @DisplayName("validates timestamps successfully")
    public void validateTimestamps(long timestamp) {
        assertDoesNotThrow(() -> resultRequestValidator.validateTimestamp(timestamp));
    }

    @ParameterizedTest
    @ValueSource(ints = {-3, -2, -1, 0, 1001, 1002, 1003})
    @DisplayName("throws exception for negative/zero limit and greater than 1000")
    public void validateInvalidLimit(int limit) {
        assertThrows(ParamValidationException.class, () -> resultRequestValidator.validateLimit(limit));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 200, 300, 999})
    @DisplayName("validates limit successfully")
    public void validateLimit(int limit) {
        assertDoesNotThrow(() -> resultRequestValidator.validateLimit(limit));
    }

    public static List<Long> provideFutureTimestamps() {
        long currentTimestamp = System.currentTimeMillis();
        return List.of(currentTimestamp + 10000, currentTimestamp + 20000, currentTimestamp + 30000);
    }

}
