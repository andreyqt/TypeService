package holymagic.typeservice.repository;

import holymagic.typeservice.model.leaderboard.RankedRace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaderboardRepository extends JpaRepository<RankedRace, Integer> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM leaderboards_60s)", nativeQuery = true)
    boolean hasAnyRecords();

    @Query(value = "SELECT l.* FROM leaderboards_60s l where l.rank = :rank", nativeQuery = true)
    RankedRace findByRank(int rank);

}
