package holymagic.typeservice.repository;

import holymagic.typeservice.model.leaderboard.RankedRace15;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface Leaderboard15Repository extends JpaRepository<RankedRace15, Integer> {

    @Query(value = "SELECT EXISTS(SELECT 1 FROM leaderboards_15s)", nativeQuery = true)
    boolean hasAnyRecords();

    @Query(value = "SELECT l.* FROM leaderboards_15s l where l.rank = :rank", nativeQuery = true)
    RankedRace15 findByRank(int rank);

}
