package holymagic.typeservice.validator;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class PublicDataValidatorTest {

    private PublicDataValidator validator = new PublicDataValidator();

    @ParameterizedTest
    @MethodSource("validLanguages")
    public void validateGetHistogramLanguageArgTest(String input) {
        assertDoesNotThrow(() -> {
            validator.validateGetHistogramArgs(input, "time", "15");
        });
    }

    @ParameterizedTest
    @MethodSource("invalidLanguages")
    public void validateGetHistogramLanguageInvalidArgTest(String input) {
        assertThrows(IllegalArgumentException.class, () -> {
            validator.validateGetHistogramArgs(input, "time", "15");
        });
    }

    @ParameterizedTest
    @MethodSource("provideValidModeCombinations")
    public void validateGetHistogramModeCombinationTest(String mode, String mode2) {
        assertDoesNotThrow(() -> {
            validator.validateGetHistogramArgs("english", mode, mode2);
        });
    }

    @ParameterizedTest
    @MethodSource("provideInvalidModeCombinations")
    public void validateGetHistogramModeInvalidModeTest(String mode, String mode2) {
        assertThrows(IllegalArgumentException.class, () -> {
            validator.validateGetHistogramArgs("english", mode, mode2);
        });
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

}
