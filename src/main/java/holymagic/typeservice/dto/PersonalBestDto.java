package holymagic.typeservice.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PersonalBestDto {
    private Double accuracy;
    private Double speed;
    private String language;
    private Boolean punctuation;
    private Boolean numbers;
    private LocalDateTime localDateTime;
}
