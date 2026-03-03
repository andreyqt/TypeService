package holymagic.typeservice.repository;

import holymagic.typeservice.model.result.DailyAverageResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface DailyResultRepository extends JpaRepository<DailyAverageResult, LocalDate> {

    @Query(nativeQuery = true, value = """
            SELECT * FROM daily_average_result WHERE date =: date
            """)
    Optional<DailyAverageResult> findByDate(@Param("date") LocalDate date);

}
