package holymagic.typeservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalBestId implements Serializable {
    private String language;
    private String mode;
    private String mode2;
    private Boolean punctuation;
    private Boolean numbers;
}
