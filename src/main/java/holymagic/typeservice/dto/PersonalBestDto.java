package holymagic.typeservice.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PersonalBestDto {
    private Double accuracy;
    private String mode;
    private String mode2;
    private Double speed;
    private String language;
    private Boolean punctuation;
    private Boolean numbers;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime localDateTime;
}
