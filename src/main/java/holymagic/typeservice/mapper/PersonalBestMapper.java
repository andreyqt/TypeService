package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.PersonalBestDto;
import holymagic.typeservice.model.user.PersonalBest;
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
public interface PersonalBestMapper {

    @Mappings({
            @Mapping(source = "acc", target = "accuracy"),
            @Mapping(source = "wpm", target = "speed"),
            @Mapping(source = "timestamp",target = "localDateTime", qualifiedByName = "mapLongToLocalDateTime")
    })
    PersonalBestDto toDto(PersonalBest personalBest);

    @Named("mapLongToLocalDateTime")
    default LocalDateTime mapLongToLocalDateTime(Long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    default List<PersonalBestDto> toDto(List<PersonalBest> personalBests) {
        return personalBests.stream().map(this::toDto).toList();
    }

}
