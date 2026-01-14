package holymagic.typeservice.validator;

import holymagic.typeservice.exception.DataValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HttpParamValidator {

    @Value("${onOrAfterTimestamp}")
    private Long initialTimestamp;
    @Value("${max_limit}")
    private int maxLimit;
    @Value("${max_page_number}")
    private int maxPageNumber;
    @Value("${max_page_size}")
    private int maxPageSize;


    public void validateTimestamp(Long timestamp) {
        if (timestamp < initialTimestamp) {
            throw new DataValidationException("timestamp must be >= " + initialTimestamp);
        }
        long currentTimestamp = System.currentTimeMillis();
        if (timestamp > currentTimestamp) {
            throw new DataValidationException("timestamp is not reached yet");
        }
    }

    public void validateOffset(Integer offset) {
        if (offset < 0 || offset > maxLimit) {
            throw new DataValidationException("offset can't be negative or exceed " + maxLimit);
        }
    }

    public void validateLimit(Integer limit) {
        if (limit <= 0 || limit > maxLimit) {
            throw new IllegalArgumentException("limit must be between 0 and " + maxLimit);
        }
    }

    public void validatePageNumber(int page) {
        if (page < 0 || page > maxPageNumber) {
            throw new IllegalArgumentException("Invalid page number: " + page);
        }
    }

    public void validatePageSize(int pageSize) {
        if (pageSize < 1 || pageSize > maxPageSize)
            throw new IllegalArgumentException("Invalid page size: " + pageSize);
    }

}
