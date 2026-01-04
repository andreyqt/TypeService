package holymagic.typeservice.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LeaderboardValidator {

    @Value("${max_page_number}")
    private int maxPageNumber;

    @Value("${max_page_size}")
    private int maxPageSize;

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
