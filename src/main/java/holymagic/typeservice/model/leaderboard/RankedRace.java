package holymagic.typeservice.model.leaderboard;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "leaderboards_english_60s")
public class RankedRace {
    @Id
    @Column(name = "uid")
    private String uid;

    @Column(name = "wpm")
    private Double wpm;

    @Column(name = "acc")
    private Double acc;

    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name = "raw")
    private Double raw;

    @Column(name = "consistency")
    private Double consistency;

    @Column(name = "name")
    private String name;

    @Column(name = "discord_Id")
    private String discordId;

    @Column(name = "discord_avatar")
    private String discordAvatar;

    @Column(name = "rank")
    private Integer rank;

    @Transient
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer friendsRank;
}
