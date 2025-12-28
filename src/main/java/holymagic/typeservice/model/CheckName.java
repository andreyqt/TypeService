package holymagic.typeservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckName {
    @JsonProperty("available")
    private Boolean isAvailable;
}
