package holymagic.typeservice.validator;

import holymagic.typeservice.exception.ParamValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

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
        assertThrows(ParamValidationException.class, () -> validator.validateLanguage(language));
    }

    @Test
    @DisplayName("validates language successfully")
    public void validateLanguage() {
        assertDoesNotThrow(() -> validator.validateLanguage("english"));
    }

    public

}
