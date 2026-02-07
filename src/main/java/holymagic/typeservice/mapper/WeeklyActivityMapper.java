package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.model.leaderboard.WeeklyActivity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface WeeklyActivityMapper {

    @Mapping(source = "lastActivityTimestamp", target = "lastActivityTimestamp",
            qualifiedByName = "mapLongToLocalDateTime")
    WeeklyActivityDto toDto(WeeklyActivity weeklyActivity);

    @Named("mapLongToLocalDateTime")
    default LocalDateTime mapLongToLocalDateTime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    default List<WeeklyActivityDto> toDto(List<WeeklyActivity> weeklyActivities) {
        return weeklyActivities.stream().map(this::toDto).toList();
    }

}
