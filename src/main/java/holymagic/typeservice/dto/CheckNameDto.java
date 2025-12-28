package holymagic.typeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CheckNameDto {
    private String message;
    private Boolean isAvailable;
}
