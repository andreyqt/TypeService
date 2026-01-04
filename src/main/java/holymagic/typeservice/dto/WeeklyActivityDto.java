package holymagic.typeservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeeklyActivityDto {
    private String uid;
    private String name;
    private LocalDateTime lastActivityTimestamp;
    private Double timeTypedSeconds;
    private String discordId;
    private Long badgeId;
    private Integer rank;
    private Integer totalXp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer friendsRank;
}
