package holymagic.typeservice.service;

import holymagic.typeservice.model.leaderboard.RankedRace;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Service
@Slf4j
public class LeaderboardCache {

    @Value("${leaderboard_cache_size}")
    private int capacity;

    private static final TreeMap<Integer, RankedRace> LEADERBOARDS = new TreeMap<>();

    public RankedRace getRace(int rank) {
        return LEADERBOARDS.get(rank);
    }

    public List<RankedRace> getAll() {
        return new ArrayList<>(LEADERBOARDS.values());
    }

    public void update(List<RankedRace> races) {
        LEADERBOARDS.clear();
        for (RankedRace race : races) {
            LEADERBOARDS.put(race.getRank(), race);
        }
    }

    public void clear() {
        LEADERBOARDS.clear();
    }

    public int getSize() {
        return LEADERBOARDS.size();
    }

}
