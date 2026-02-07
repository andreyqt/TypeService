package holymagic.typeservice.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStats {
    @JsonProperty("_id")
    private String id;
    private Integer completedTests;
    private Integer startedTests;
    private Double timeTyping;
}
