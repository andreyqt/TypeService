package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.RaceDto;
import holymagic.typeservice.model.race.Race;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RaceMapper {
    @Mappings({
            @Mapping(source = "acc", target = "accuracy"),
            @Mapping(source = "wpm", target = "speed"),
            @Mapping(source = "timestamp", target = "localDateTime", qualifiedByName = "mapLongToLocalDateTime"),
            @Mapping(source = "charStats", target = "chars", qualifiedByName = "mapCharStatsToChars")
    })
    RaceDto toDto(Race race);

    @Named("mapLongToLocalDateTime")
    default LocalDateTime mapLongToLocalDateTime(Long id) {
        Instant instant = Instant.ofEpochMilli(id);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    @Named("mapCharStatsToChars")
    default int mapCharStatsToChars(int[] charStats) {
        return charStats[0];
    }

    default List<RaceDto> toDto(List<Race> races) {
        return races.stream().map(this::toDto).toList();
    }
}
