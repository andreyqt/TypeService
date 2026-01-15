package holymagic.typeservice.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Modes {

    public static final Set<String> ALLOWED_LANGUAGES = Set.of(
            "english", "english_1k", "english_5k", "english_10k", "english_25k", "english_450k",
            "russian", "russian_1k", "russian_5k", "russian_10k", "russian_25k", "russian_50k"
    );

    public static final Map<String, List<String>> ALLOWED_MODES = Map.of(
            "time", List.of("15", "30", "60", "120"),
            "words", List.of("10", "25", "50", "100")
    );

}
