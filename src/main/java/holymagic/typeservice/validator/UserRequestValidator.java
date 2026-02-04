package holymagic.typeservice.validator;

import holymagic.typeservice.exception.DataValidationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class UserRequestValidator {

    private static final Map<String, List<String>> ALLOWED_MODES_FOR_PERSONAL_BESTS = Map.of(
            "time", List.of("15", "30", "60", "120"),
            "words", List.of("10", "25", "50", "100")
    );

    public static final Set<String> ALLOWED_LANGUAGES_FOR_PERSONAL_BESTS = Set.of(
            "english", "english1k", "english5k", "english10k", "english25k", "english450k",
            "russian", "russian1k", "russian5k", "russian10k", "russian25k", "russian50k"
    );

    public void validateLanguage(String language) {
        if (!ALLOWED_LANGUAGES_FOR_PERSONAL_BESTS.contains(language)) {
            throw new DataValidationException("Invalid language " + language);
        }
    }

    public void validateModeCombination(String mode, String mode2) {
        if (!ALLOWED_MODES_FOR_PERSONAL_BESTS.containsKey(mode) ||
        !ALLOWED_MODES_FOR_PERSONAL_BESTS.get(mode).contains(mode2)) {
            throw new DataValidationException(String.format("invalid mode combination: %s, %s", mode, mode2));
        }
    }

}
