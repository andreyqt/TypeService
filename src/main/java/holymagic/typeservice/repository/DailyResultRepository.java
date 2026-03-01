package holymagic.typeservice.repository;

import holymagic.typeservice.model.result.DailyAverageResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface DailyResultRepository extends JpaRepository<DailyAverageResult, LocalDate> {
}
