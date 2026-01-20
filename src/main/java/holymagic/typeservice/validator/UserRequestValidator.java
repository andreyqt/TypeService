package holymagic.typeservice.validator;

import holymagic.typeservice.exception.DataValidationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UserRequestValidator {

    private static final Map<String, List<String>> ALLOWED_MODES_FOR_PERSONAL_BESTS = Map.of(
            "time", List.of("15", "30", "60", "120"),
            "words", List.of("10", "25", "50", "100")
    );

    public void validateMode(String mode) {
        if (!ALLOWED_MODES_FOR_PERSONAL_BESTS.containsKey(mode)) {
            throw new DataValidationException("invalid mode for personal best: " + mode);
        }
    }

    public void validateModeCombination(String mode, String mode2) {
        if (!ALLOWED_MODES_FOR_PERSONAL_BESTS.containsKey(mode) ||
        !ALLOWED_MODES_FOR_PERSONAL_BESTS.get(mode).contains(mode2)) {
            throw new DataValidationException(String.format("invalid mode combination: %s, %s", mode, mode2));
        }
    }

}
