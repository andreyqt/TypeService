package holymagic.typeservice.repository;

import holymagic.typeservice.model.race.Race;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaceRepository extends JpaRepository<Race, String> {
}
