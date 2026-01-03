package holymagic.typeservice.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LeaderboardValidatorTest {
    private LeaderboardValidator leaderboardValidator;

    @BeforeEach
    public void setUp() {
        leaderboardValidator = new LeaderboardValidator();
        ReflectionTestUtils.setField(leaderboardValidator, "maxPageNumber", 1000);
        ReflectionTestUtils.setField(leaderboardValidator, "maxPageSize", 200);
    }

    @ParameterizedTest
    @ValueSource(ints = {-3, -2, -1, 1001, 1002, 1003})
    public void validateInvalidPageNumber(int pageNumber) {
        assertThrows(IllegalArgumentException.class, () -> {
            leaderboardValidator.validatePageNumber(pageNumber);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0,1,2,3,999,1000})
    public void validatePageNumberNumber(int pageNumber) {
        assertDoesNotThrow(() -> {
            leaderboardValidator.validatePageNumber(pageNumber);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {-1,0,201,202})
    public void validateInvalidPageSize(int pageSize) {
        assertThrows(IllegalArgumentException.class, () -> {
            leaderboardValidator.validatePageSize(pageSize);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,3,199,200})
    public void validatePageSize(int pageSize) {
        assertDoesNotThrow(() -> {
            leaderboardValidator.validatePageSize(pageSize);
        });
    }
}
