package holymagic.typeservice.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RaceValidator {
    @Value("${onOrAfterTimestamp}")
    private Long initialTimestamp;

    @Value("${max_limit}")
    private int maxLimit;

    public void validateTimestamp(Long timestamp) {
        if (timestamp < initialTimestamp) {
            throw new IllegalArgumentException("timestamp must be >= " + initialTimestamp);
        }
    }

    public void validateOffset(Integer offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset can't be negative");
        }
    }

    public void validateLimit(Integer limit) {
        if (limit <= 0 || limit > maxLimit) {
            throw new IllegalArgumentException("limit must be between 0 and " + maxLimit);
        }
    }
}
