package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.RankingDto;
import holymagic.typeservice.model.leaderboard.Ranking;
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
public interface RankingMapper {

    @Mappings({
            @Mapping(source = "acc", target = "accuracy"),
            @Mapping(source = "wpm", target = "speed"),
            @Mapping(source = "timestamp", target = "localDateTime", qualifiedByName = "mapLongToLocalDateTime")
    })
    RankingDto toDto(Ranking ranking);


    @Named("mapLongToLocalDateTime")
    default LocalDateTime mapLongToLocalDateTime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    default List<RankingDto> toDto(List<Ranking> rankings) {
        return rankings.stream().map(this::toDto).toList();
    }

}
