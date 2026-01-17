package holymagic.typeservice.validator;

import holymagic.typeservice.exception.DataValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class LeaderboardRequestValidator {

    @Value("${max_page_number}")
    private int maxPageNumber;
    @Value("${max_page_size}")
    private int maxPageSize;
    @Value("${min_page_size}")
    private int minPageSize;

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

    public void ValidateGlobalLeaderboardArgs(String language, String mode, String mode2,
                                              Integer page, Integer pageSize) {
        if (!GLOBAL_ALLOWED_LANGUAGES.contains(language)) {
            throw new DataValidationException("invalid language for global lbs: " + language);
        }
        validateModes(mode, mode2);
        validatePage(page);
        validatePageSize(pageSize);
    }

    public void validateDailyLeaderboardArgs(String language, String mode, String mode2,
                                             Integer page, Integer pageSize) {
        if (!DAILY_ALLOWED_LANGUAGES.contains(language)) {
            throw new DataValidationException("invalid language: " + language);
        }
        validateModes(mode, mode2);
        validatePage(page);
        validatePageSize(pageSize);
    }

    public void validateModes(String mode, String mode2) {
        if (!ALLOWED_MODES_FOR_LEADERBOARDS.containsKey(mode) ||
                !ALLOWED_MODES_FOR_LEADERBOARDS.get(mode).contains(mode2)) {
            throw new DataValidationException("invalid mode combination: " + mode + " " + mode2);
        }
    }

    public void validatePage(Integer page) {
        if (page == null) {
            return;
        }
        if (page < 0 || page > maxPageNumber) {
            throw new DataValidationException("Invalid page number: " + page);
        }
    }

    public void validatePageSize(Integer pageSize) {
        if (pageSize == null) {
            return;
        }
        if (pageSize < minPageSize || pageSize > maxPageSize)
            throw new DataValidationException("Invalid page size: " + pageSize);
    }

}
