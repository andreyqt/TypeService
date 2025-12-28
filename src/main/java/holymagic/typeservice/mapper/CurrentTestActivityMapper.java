package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.CurrentTestActivityDto;
import holymagic.typeservice.model.user.CurrentTestActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CurrentTestActivityMapper {
    @Mapping(source = "lastDay", target = "lastDay", qualifiedByName = "mapLongToLocalDateTime")
    CurrentTestActivityDto toDto(CurrentTestActivity currentTestActivity);

    @Named("mapLongToLocalDateTime")
    default LocalDateTime mapLongToLocalDateTime(Long lastDay) {
        Instant instant = Instant.ofEpochMilli(lastDay);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
