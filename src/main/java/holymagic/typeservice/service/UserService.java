package holymagic.typeservice.service;

import holymagic.typeservice.dto.CheckNameDto;
import holymagic.typeservice.dto.PersonalBestDto;
import holymagic.typeservice.mapper.CheckNameMapper;
import holymagic.typeservice.mapper.PersonalBestMapper;
import holymagic.typeservice.model.CheckName;
import holymagic.typeservice.model.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static holymagic.typeservice.model.ParameterizedTypeReferences.CHECK_NAME_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.LIST_OF_RECORDS;
import static holymagic.typeservice.model.ParameterizedTypeReferences.MAP_OF_LIST_OF_RECORDS;

@Service
@RequiredArgsConstructor
public class UserService {
    private final RestClient restClient;
    private final CheckNameMapper checkNameMapper;
    private final PersonalBestMapper personalBestMapper;

    public CheckNameDto checkName(String name) {
        URI uri = UriBuilder.fromPath("users/checkName/{name}")
                .build(name);
        Response<CheckName> response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(CHECK_NAME_REF);
        return checkNameMapper.toDto(response);
    }

    public Map<String, List<PersonalBestDto>> getPersonalBests(String mode) {
        URI uri = UriBuilder.fromPath("/users/personalBests")
                .queryParam("mode", mode)
                .build();
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(MAP_OF_LIST_OF_RECORDS)
                .getData()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> personalBestMapper.toDto(entry.getValue())
                ));
    }

    public List<PersonalBestDto> getPersonalBests(String mode, String mode2) {
        URI uri = UriBuilder.fromPath("/users/personalBests")
                .queryParam("mode", mode)
                .queryParam("mode2", mode2)
                .build();
        return restClient.get()
                .uri(uri)
                .retrieve()
                .body(LIST_OF_RECORDS)
                .getData()
                .stream()
                .map(personalBestMapper::toDto)
                .toList();
    }
}
