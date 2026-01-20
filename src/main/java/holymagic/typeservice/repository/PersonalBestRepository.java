package holymagic.typeservice.repository;

import holymagic.typeservice.model.race.PersonalBest;
import holymagic.typeservice.model.race.PersonalBestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalBestRepository extends JpaRepository<PersonalBest, PersonalBestId> {

}
