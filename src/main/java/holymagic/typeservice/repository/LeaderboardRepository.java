package holymagic.typeservice.repository;

import holymagic.typeservice.model.leaderboard.RankedRace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardRepository extends JpaRepository<RankedRace, Integer> {

}
