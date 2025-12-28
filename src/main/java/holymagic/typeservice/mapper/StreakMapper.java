package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.StreakDto;
import holymagic.typeservice.model.user.Streak;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StreakMapper {
    @Mapping(source = "lastResultTimestamp", target = "lastResultTimestamp", qualifiedByName = "mapLongToLocalDateTime")
    StreakDto ToDto(Streak streak);

    @Named("mapLongToLocalDateTime")
    default LocalDateTime mapLongToLocalDateTime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
