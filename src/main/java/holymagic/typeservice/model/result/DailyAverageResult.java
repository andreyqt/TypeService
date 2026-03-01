package holymagic.typeservice.model.result;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "daily_average_result")
public class DailyAverageResult {

    @Id
    @Column(name = "date")
    private LocalDate localDate;

    @Column(name = "average_wmp")
    private double averageWmp;

    @Column(name = "average_acc")
    private double averageAcc;

    @Column(name = "num_of_tests")
    private int numOfTests;

    @Column(name = "time")
    private double time;

}
