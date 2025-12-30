package holymagic.typeservice.model.race;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Race {
    @JsonProperty("_id")
    private String id;
    private String uid;
    private int[] charStats = new int[4];
    private Double wpm;
    private Double acc;
    private String mode;
    private String mode2;
    private Long timestamp;
    private Double testDuration;
}
