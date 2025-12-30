package holymagic.typeservice.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class RaceValidatorTest {
    private RaceValidator raceValidator;

    @BeforeEach
    public void setUp() {
        raceValidator = new RaceValidator();
        ReflectionTestUtils.setField(raceValidator, "maxLimit", 1000);
        ReflectionTestUtils.setField(raceValidator, "initialTimestamp", 1589428800000L);
    }

    @Test
    public void ValidateTimestampAndLimitAndOffsetTest() {
        Assertions.assertDoesNotThrow(() -> {
            raceValidator.validateTimestamp(1589428800001L);
            raceValidator.validateTimestamp(1689428800000L);
            raceValidator.validateOffset(5);
            raceValidator.validateOffset(0);
            raceValidator.validateLimit(100);
            raceValidator.validateLimit(1);
        });
    }

    @Test
    public void ValidateIllegalTimestampTest() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            raceValidator.validateTimestamp(1589428799999L);
        });
        Assertions.assertEquals("timestamp must be >= " + 1589428800000L, exception.getMessage());
    }

    @Test
    public void ValidateIllegalOffsetTest() {
        Exception exception = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            raceValidator.validateOffset(-1);
        });
        Assertions.assertEquals("offset can't be negative", exception.getMessage());
    }

    @Test
    public void ValidateIllegalLimitTest() {
        Exception exceptionWithNegativeLimit = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            raceValidator.validateLimit(-1);
        });
        Exception exceptionWithOutOfRangeLimit = Assertions.assertThrows(IllegalArgumentException.class, () -> {
            raceValidator.validateLimit(1001);
        });
        Assertions.assertEquals("limit must be between 0 and " + 1000,
                exceptionWithNegativeLimit.getMessage());
        Assertions.assertEquals("limit must be between 0 and " + 1000,
                exceptionWithOutOfRangeLimit.getMessage());
    }
}
