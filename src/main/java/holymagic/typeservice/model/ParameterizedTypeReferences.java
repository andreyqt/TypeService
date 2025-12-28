package holymagic.typeservice.model;

import holymagic.typeservice.model.race.PersonalBest;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Map;

public class ParameterizedTypeReferences {

    public static final ParameterizedTypeReference<Response<CheckName>> CHECK_NAME_REF =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<Map<String, List<PersonalBest>>>> MAP_OF_LIST_OF_RECORDS =
            new ParameterizedTypeReference<>() {};

    public static final ParameterizedTypeReference<Response<List<PersonalBest>>> LIST_OF_RECORDS =
            new ParameterizedTypeReference<>() {};
}
