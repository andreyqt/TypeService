package holymagic.typeservice.validator;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class PublicDataValidator {

    private static final Set<String> ALLOWED_LANGUAGES = Set.of(
            "english", "english_1k", "english_5k", "english_10k", "english_25k", "english_450k",
            "russian", "russian_1k", "russian_5k", "russian_10k", "russian_25k", "russian_50k"
    );

    private static final Map<String, List<String>> ALLOWED_MODES = Map.of(
            "time", List.of("15", "30", "60", "120"),
            "words", List.of("10", "25", "50", "100")
    );

    public void validatePublicDataArgs(String language, String mode, String mode2) {
        if (!ALLOWED_LANGUAGES.contains(language)) {
            throw new IllegalArgumentException("can't provide information for given language: " + language);
        }
        if (!ALLOWED_MODES.containsKey(mode)) {
            throw new IllegalArgumentException("can't provide information for given mode: " + mode);
        }
        if (!ALLOWED_MODES.get(mode).contains(mode2)) {
            throw new IllegalArgumentException("illegal mode combination: " + mode + " + " + mode2);
        }
    }

}
