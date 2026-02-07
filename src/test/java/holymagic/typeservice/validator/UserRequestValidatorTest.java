package holymagic.typeservice.validator;

import holymagic.typeservice.exception.ParamValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
public class UserRequestValidatorTest {

    private UserRequestValidator userRequestValidator;

    @BeforeEach
    public void setUp() {
        userRequestValidator = new UserRequestValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"english", "english1k", "russian", "russian1k"})
    @DisplayName("validates languages successfully")
    public void validateLanguages(String language) {
        assertDoesNotThrow(() -> userRequestValidator.validateLanguage(language));
    }

    @ParameterizedTest
    @ValueSource(strings = {"english2k", "english_old", "russian_old"})
    @DisplayName("throws exception for invalid languages")
    public void validateInvalidLanguages(String language) {
        assertThrows(ParamValidationException.class,
                () -> userRequestValidator.validateLanguage(language));
    }

    @ParameterizedTest
    @MethodSource("provideValidModeCombinations")
    @DisplayName("validates allowed mode combinations")
    public void validateAllowedModeCombinations(String mode, String mode2) {
        assertDoesNotThrow(() -> userRequestValidator.validateModeCombination(mode, mode2));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidModeCombinations")
    @DisplayName("throws exception for invalid mode combinations")
    public void validateInvalidModeCombinations(String mode, String mode2) {
        assertThrows(ParamValidationException.class,
                () -> userRequestValidator.validateModeCombination(mode, mode2));
    }

    private static Stream<Arguments> provideValidModeCombinations() {
        return Stream.of(
                Arguments.of("time", "15"),
                Arguments.of("time", "60"),
                Arguments.of("words", "10"),
                Arguments.of("words", "50")
        );
    }

    private static Stream<Arguments> provideInvalidModeCombinations() {
        return Stream.of(
                Arguments.of("quote", "30"),
                Arguments.of("time", "55"),
                Arguments.of("words", "15"),
                Arguments.of("zen", "25")
        );
    }

}
