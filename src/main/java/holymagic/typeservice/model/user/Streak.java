package holymagic.typeservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Streak {
    private Long lastResultTimestamp;
    private Integer length;
    private Integer maxLength;
    private Integer hourOffset;
}
