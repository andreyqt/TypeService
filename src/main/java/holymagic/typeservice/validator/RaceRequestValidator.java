package holymagic.typeservice.validator;

import holymagic.typeservice.exception.DataValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RaceRequestValidator {

    @Value("${onOrAfterTimestamp}")
    private Long initialTimestamp;
    @Value("${max_limit}")
    private int maxLimit;

    public void validateTimestamp(Long timestamp) {
        if (timestamp == null) {
            return;
        }
        if (timestamp < initialTimestamp) {
            throw new DataValidationException("timestamp must be >= " + initialTimestamp);
        }
        long currentTimestamp = System.currentTimeMillis();
        if (timestamp > currentTimestamp) {
            throw new DataValidationException("timestamp is not reached yet");
        }
    }

    public void validateOffset(Integer offset) {
        if (offset == null) {
            return;
        }
        if (offset < 0 || offset >= maxLimit) {
            throw new DataValidationException("offset can't be negative or exceed " + maxLimit);
        }
    }

    public void validateLimit(Integer limit) {
        if (limit == null) {
            return;
        }
        if (limit <= 0 || limit > maxLimit) {
            throw new DataValidationException("limit must be between 0 and " + maxLimit);
        }
    }

    public void validateGetResultsArgs(Long onOrAfterTimestamp, Integer offset, Integer limit) {
        validateTimestamp(onOrAfterTimestamp);
        validateOffset(offset);
        validateLimit(limit);
    }

}
