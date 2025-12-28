package holymagic.typeservice.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrentTestActivity {
    private List<Integer> testsByDays;
    private Long lastDay;
}
