package holymagic.typeservice.validator;

import holymagic.typeservice.exception.ParamValidationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PublicDataRequestValidator {

    private static final Map<String, List<String>> ALLOWED_MODES_SPEED_HISTOGRAM = Map.of(
            "time", List.of("15", "60")
    );

    public void validateModeCombination(String mode, String mode2) {
        if (!ALLOWED_MODES_SPEED_HISTOGRAM.containsKey(mode) ||
            !ALLOWED_MODES_SPEED_HISTOGRAM.get(mode).contains(mode2)) {
            throw new ParamValidationException("invalid mode combination for public data request");
        }
    }

}
