package holymagic.typeservice.mapper;

import holymagic.typeservice.dto.CheckNameDto;
import holymagic.typeservice.model.user.CheckName;
import holymagic.typeservice.model.Response;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CheckNameMapper {
    @Mapping(source = "data", target = "isAvailable", qualifiedByName = "mapDataToBoolean")
    CheckNameDto toDto(Response<CheckName> response);

    @Named("mapDataToBoolean")
    default Boolean mapDataToBoolean(CheckName checkName) {
        return checkName.getIsAvailable();
    }

}
