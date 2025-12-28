package holymagic.typeservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Details {
    private String bio;
    private String keyboard;
    private Map<String, String> socialProfiles;
}
