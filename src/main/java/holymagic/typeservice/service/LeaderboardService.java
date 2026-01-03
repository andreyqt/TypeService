package holymagic.typeservice.service;

import holymagic.typeservice.dto.RankedRaceDto;
import holymagic.typeservice.mapper.RankedRaceMapper;
import holymagic.typeservice.model.leaderboard.Leaderboard;
import holymagic.typeservice.validator.LeaderboardValidator;
import holymagic.typeservice.validator.PublicDataValidator;
import jakarta.annotation.Nullable;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LEADERBOARD_REF;

@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final RestClient restClient;
    private final RankedRaceMapper rankedRaceMapper;
    private final PublicDataValidator publicDataValidator;
    private final LeaderboardValidator leaderboardValidator;

    public List<RankedRaceDto> getLeaderboard(String language, String mode, String mode2,
                                              @Nullable Integer page, @Nullable Integer pageSize,
                                              @Nullable Boolean friendsOnly) {
        URI uri = prepareGetLeaderboardUri(language, mode, mode2, page, pageSize, friendsOnly);

        Leaderboard leaderboard = restClient.get()
                .uri(uri)
                .retrieve()
                .body(LEADERBOARD_REF)
                .getData();

        if (leaderboard == null) {
            throw new NotFoundException(String.format(
                    "could not get the leaderboard for args: language=%s, mode=%s, mode2=%s, page=%s, pageSize=%s, friendsOnly=%s",
                    language, mode, mode2, page, pageSize, friendsOnly
            ));
        }

        return rankedRaceMapper.toDto(leaderboard.getEntries());
    }

    private URI prepareGetLeaderboardUri(String language, String mode, String mode2,
                                         @Nullable Integer page, @Nullable Integer pageSize,
                                         @Nullable Boolean friendsOnly) {
        UriBuilder builder = UriBuilder.fromPath("/leaderboards");

        publicDataValidator.validatePublicDataArgs(language, mode, mode2);
        builder.queryParam("language", language);
        builder.queryParam("mode", mode);
        builder.queryParam("mode2", mode2);

        if (page != null) {
            leaderboardValidator.validatePageNumber(page);
            builder.queryParam("page", page);
        }
        if (pageSize != null) {
            leaderboardValidator.validatePageSize(pageSize);
            builder.queryParam("pageSize", pageSize);
        }
        if (friendsOnly != null) {
            builder.queryParam("friendsOnly", true);
        }
        return builder.build();
    }

}
