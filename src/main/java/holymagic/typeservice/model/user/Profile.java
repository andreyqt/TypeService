package holymagic.typeservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
    private String name;
    private Long addedAt;
    private UserStats typingStats;
    private Map<String, Map<String, List<PersonalBest>>> personalBests;
    private Long discordId;
    private String discordAvatar;
    private Integer xp;
    private Integer streak;
    private Integer maxStreak;
    private Boolean isPremium;
    private Details details;
    private Map<String, Map<String, Map<String,LanguageRank>>> allTimeLbs;
    private String uid;
}
