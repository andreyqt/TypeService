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
public class RankedRace {
    private Double wpm;
    private Double acc;
    private Long timestamp;
    private Double raw;
    private Double consistency;
    private String uid;
    private String name;
    private String discordId;
    private String discordAvatar;
    private Integer rank;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer friendsRank;
}
