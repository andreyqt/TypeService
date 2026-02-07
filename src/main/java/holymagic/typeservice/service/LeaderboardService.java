package holymagic.typeservice.service;

import holymagic.typeservice.dto.RankingDto;
import holymagic.typeservice.dto.WeeklyActivityDto;
import holymagic.typeservice.mapper.RankingMapper;
import holymagic.typeservice.mapper.WeeklyActivityMapper;
import holymagic.typeservice.model.leaderboard.Leaderboard;
import holymagic.typeservice.model.leaderboard.Ranking;
import holymagic.typeservice.model.leaderboard.WeeklyActivity;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

import static holymagic.typeservice.model.ParameterizedTypeReferences.LEADERBOARD_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.RANKING_RACE_REF;
import static holymagic.typeservice.model.ParameterizedTypeReferences.XP_LEADERBOARD_REF;

@Slf4j
@Service
@RequiredArgsConstructor
public class LeaderboardService {

    private final WeeklyActivityMapper weeklyActivityMapper;
    private final ExchangeService exchangeService;
    private final RankingMapper rankingMapper;

    @Value("${default_page_size}")
    private int pageSize;

    public List<RankingDto> getLeaderboard(String language, String mode,
                                           String mode2, boolean friendsOnly) {
        URI uri = provideUri("/leaderboards", language, mode, mode2, friendsOnly);
        Leaderboard leaderboard = exchangeService.makeGetRequest(uri, LEADERBOARD_REF);
        return rankingMapper.toDto(leaderboard.getEntries());
    }

    public RankingDto getRank(String language, String mode, String mode2, boolean friendsOnly) {
        URI uri = provideUri("/leaderboards/rank", language, mode, mode2, friendsOnly);
        Ranking ranking = exchangeService.makeGetRequest(uri, RANKING_RACE_REF);
        return rankingMapper.toDto(ranking);
    }

    public List<RankingDto> getDailyLeaderboard(String language, String mode, String mode2, boolean friendsOnly) {
        URI uri = provideUri("/leaderboards/daily", language, mode, mode2, friendsOnly);
        Leaderboard leaderboard = exchangeService.makeGetRequest(uri, LEADERBOARD_REF);
        return rankingMapper.toDto(leaderboard.getEntries());
    }

    public List<WeeklyActivityDto> getWeeklyXpLeaderboard(boolean friendsOnly) {
        URI uri = UriBuilder.fromPath("/leaderboards/xp/weekly")
                .queryParam("friendsOnly", friendsOnly)
                .build();
        List<WeeklyActivity> activity = exchangeService.makeGetRequest(uri, XP_LEADERBOARD_REF)
                .getEntries();
        return weeklyActivityMapper.toDto(activity);
    }

    private URI provideUri(String path, String language, String mode, String mode2, boolean friendsOnly) {
        return UriBuilder.fromPath(path)
                .queryParam("language", language)
                .queryParam("mode", mode)
                .queryParam("pageSize", pageSize)
                .queryParam("mode2", mode2)
                .queryParam("friendsOnly", friendsOnly)
                .build();
    }

}
