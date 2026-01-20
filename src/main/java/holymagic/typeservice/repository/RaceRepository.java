package holymagic.typeservice.repository;

import holymagic.typeservice.model.race.Race;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceRepository extends JpaRepository<Race, String> {

    Race findByTimestamp(Long timestamp);

    @Query(value = "SELECT r.* FROM races r ORDER BY r.timestamp DESC LIMIT :capacity", nativeQuery = true)
    List<Race> findForCache(@Param("capacity")int cacheCapacity);

}
