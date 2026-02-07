package holymagic.typeservice.model.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ranking {
    private Integer rank;
    private String uid;
    private Double wpm;
    private Double acc;
    private Long timestamp;
    private Double raw;
    private Double consistency;
    private String name;
    private String discordId;
    private String discordAvatar;
    private Integer friendsRank;
}
