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

    public int getSize() {
        return LEADERBOARDS.size();
    }

    public RankedRace getRace(int rank) {
        return LEADERBOARDS.get(rank);
    }

    public List<RankedRace> getAll() {
        return new ArrayList<>(LEADERBOARDS.values());
    }

    public void clear() {
        LEADERBOARDS.clear();
    }

    public void add(List<RankedRace> races) {
        if (races.size() > capacity) {
            throw new IllegalArgumentException(String.format("can't add more than %d races", capacity));
        }
        for (RankedRace race : races) {
            if (race.getRank() <= capacity)
                LEADERBOARDS.put(race.getRank(), race);
        }
    }

}
