package holymagic.typeservice.model.race;

import com.fasterxml.jackson.annotation.JsonProperty;
import holymagic.typeservice.model.PostgresSQLIntArrayType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "races")
public class Race {
    @Id
    @Column(name = "id")
    @JsonProperty("_id")
    private String id;

    @Column(name = "uid")
    private String uid;

    @Column(name = "char_stats", columnDefinition = "int[]")
    @Type(value = PostgresSQLIntArrayType.class)
    private int[] charStats = new int[4];

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
