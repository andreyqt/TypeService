package holymagic.typeservice.model.leaderboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Leaderboard {
    private Integer count;
    private List<Ranking> entries;
}
