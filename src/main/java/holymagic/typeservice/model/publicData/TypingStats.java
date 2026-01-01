package holymagic.typeservice.model.publicData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TypingStats {
    private Long testsCompleted;
    private Long testsStarted;
    private Double timeTyping;
}
