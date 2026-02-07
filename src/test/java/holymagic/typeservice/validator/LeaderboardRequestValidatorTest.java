package holymagic.typeservice.validator;

import holymagic.typeservice.exception.ParamValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class LeaderboardRequestValidatorTest {

    private LeaderboardRequestValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new LeaderboardRequestValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"russian", "spanish", "french"})
    @DisplayName("throws exception for not allowed languages for global leaderboards")
    public void validateNotallowedLanguages(String language) {
        assertThrows(ParamValidationException.class, () -> validator.validateGlobalLanguage(language));
    }

    @Test
    @DisplayName("validates language successfully")
    public void validateGlobalLanguage() {
        assertDoesNotThrow(() -> validator.validateGlobalLanguage("english"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidModeCombinations")
    @DisplayName("throws exception for invalid mode combinations")
    public void validateInvalidModeCombinations(String mode, String mode2) {
        assertThrows(ParamValidationException.class, () -> validator.validateModes(mode, mode2));
    }

    @ParameterizedTest
    @MethodSource("provideValidModeCombinations")
    @DisplayName("validates mode combination successfully")
    public void validateModeCombination(String mode, String mode2) {
        assertDoesNotThrow(() -> validator.validateModes(mode, mode2));
    }

    private static Stream<Arguments> provideValidModeCombinations() {
        return Stream.of(
                Arguments.of("time", "15"),
                Arguments.of("time", "60")
        );
    }

    private static Stream<Arguments> provideInvalidModeCombinations() {
        return Stream.of(
                Arguments.of("time", "30"),
                Arguments.of("time", "120"),
                Arguments.of("words", "25")
        );
    }

}
