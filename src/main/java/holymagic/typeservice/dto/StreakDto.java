package holymagic.typeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StreakDto {
    private LocalDateTime lastResultTimestamp;
    private Integer length;
    private Integer maxLength;
    private Integer hourOffset;
}
