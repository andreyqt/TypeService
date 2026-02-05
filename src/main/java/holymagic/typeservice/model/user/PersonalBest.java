package holymagic.typeservice.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "personal_bests")
@IdClass(PersonalBestId.class)
public class PersonalBest {

    @Id
    @Column(name = "language")
    private String language;

    @Id
    @Column(name = "mode")
    private String mode;

    @Id
    @Column(name = "mode2")
    private String mode2;

    @Id
    @Column(name = "punctuation")
    private Boolean punctuation = false;

    @Id
    @Column(name = "numbers")
    private Boolean numbers = false;

    @Column(name = "acc")
    private Double acc;

    @Column(name = "wpm")
    private Double wpm;

    @Column(name = "consistency")
    private Double consistency;

    @Transient
    private String difficulty;

    @Column(name = "timestamp")
    private Long timestamp;

//    @PrePersist
//    @PreUpdate
//    public void ensureDefaults() {
//        if (this.punctuation == null) this.punctuation = false;
//        if (this.numbers == null) this.numbers = false;
//    }

}
