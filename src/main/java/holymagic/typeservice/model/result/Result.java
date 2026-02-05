package holymagic.typeservice.model.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "results")
public class Result {

    @Id
    @Column(name = "id")
    @JsonProperty("_id")
    private String id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "char_stats", columnDefinition = "integer[4]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<Integer> charStats;

    @Column(name = "wpm")
    private Double wpm;

    @Column(name = "acc")
    private Double acc;

    @Column(name = "mode")
    private String mode;

    @Column(name = "mode2")
    private String mode2;

    @Column(name = "timestamp")
    private Long timestamp;

    @Column(name = "test_duration")
    private Double testDuration;

}
