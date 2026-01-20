package holymagic.typeservice.model.leaderboard;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@AllArgsConstructor
@Entity
@Table(name = "leaderboards_15s")
public class RankedRace15 extends BaseRankedRace {

}
