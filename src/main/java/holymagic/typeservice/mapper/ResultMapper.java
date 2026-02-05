package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.ResultDto;
import holymagic.typeservice.model.result.Result;
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
public interface ResultMapper {

    @Mappings({
            @Mapping(source = "acc", target = "accuracy"),
            @Mapping(source = "wpm", target = "speed"),
            @Mapping(source = "timestamp", target = "localDateTime", qualifiedByName = "mapLongToLocalDateTime"),
            @Mapping(source = "charStats", target = "chars", qualifiedByName = "mapCharStatsToChars")
    })
    ResultDto toDto(Result result);

    @Named("mapLongToLocalDateTime")
    default LocalDateTime mapLongToLocalDateTime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    @Named("mapCharStatsToChars")
    default int mapCharStatsToChars(List<Integer> charStats) {
        return charStats.getFirst();
    }

    default List<ResultDto> toDto(List<Result> results) {
        return results.stream().map(this::toDto).toList();
    }

}
