package holymagic.typeservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class FailedResponse {
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorId;
}
