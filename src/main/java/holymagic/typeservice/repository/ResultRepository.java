package holymagic.typeservice.repository;

import holymagic.typeservice.model.result.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result, String> {

}
