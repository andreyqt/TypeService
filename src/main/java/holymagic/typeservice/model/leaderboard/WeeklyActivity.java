package holymagic.typeservice.model.leaderboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WeeklyActivity {
    private String uid;
    private String name;
    private Long lastActivityTimestamp;
    private Double timeTypedSeconds;
    private String discordId;
    private String discordAvatar;
    private Long badgeId;
    private Integer rank;
    private Integer totalXp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer friendsRank;
}
