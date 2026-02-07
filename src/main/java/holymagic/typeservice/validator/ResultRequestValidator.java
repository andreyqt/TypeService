package holymagic.typeservice.validator;

import holymagic.typeservice.exception.ParamValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResultRequestValidator {

    @Value("${onOrAfterTimestamp}")
    private Long initialTimestamp;
    @Value("${max_limit}")
    private int maxLimit;

    public void validateTimestamp(Long timestamp) {
        if (timestamp < initialTimestamp || timestamp > System.currentTimeMillis()) {
            throw new ParamValidationException("invalid timestamp: " + timestamp);
        }
    }

    public void validateLimit(Integer limit) {
        if (limit <= 0 || limit > maxLimit) {
            throw new ParamValidationException("limit must be between 0 and " + maxLimit);
        }
    }

    public void validateLimitAndTimestamp(Long timestamp, Integer limit) {
        validateLimit(limit);
        validateTimestamp(timestamp);
    }

}
