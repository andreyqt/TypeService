package holymagic.typeservice.model.race;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalBest {
    private Double acc;
    private Double wpm;
    private Double consistency;
    private String difficulty;
    private String language;
    private Boolean punctuation;
    private Boolean numbers;
    private Long timestamp;
}
