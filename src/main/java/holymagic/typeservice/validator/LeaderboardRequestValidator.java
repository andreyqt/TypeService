package holymagic.typeservice.validator;

import holymagic.typeservice.exception.ParamValidationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class LeaderboardRequestValidator {

    private static final Set<String> GLOBAL_ALLOWED_LANGUAGES = Set.of(
            "english"
    );

    private static final Map<String, List<String>> ALLOWED_MODES_FOR_LEADERBOARDS = Map.of(
            "time", List.of("15", "60")
    );

    private static final Set<String> DAILY_ALLOWED_LANGUAGES = Set.of(
            "english",
            "french",
            "german",
            "indonesian",
            "italian",
            "portuguese",
            "spanish"
    );

    public void validateLanguage(String language) {
        if (!GLOBAL_ALLOWED_LANGUAGES.contains(language)) {
            throw new ParamValidationException("Language is not valid: " + language);
        }
    }

    public void validateModes(String mode, String mode2) {
        if (!ALLOWED_MODES_FOR_LEADERBOARDS.containsKey(mode) ||
            !ALLOWED_MODES_FOR_LEADERBOARDS.get(mode).contains(mode2)) {
            throw new ParamValidationException("invalid mode combination: " + mode + " " + mode2);
        }
    }

}
