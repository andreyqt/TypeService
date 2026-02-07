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
public class RankingDto {
    private Double speed;
    private Double accuracy;
    private LocalDateTime localDateTime;
    private String uid;
    private String name;
    private String discordId;
    private Integer rank;
}
