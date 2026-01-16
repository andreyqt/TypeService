package holymagic.typeservice.cache;

import holymagic.typeservice.model.leaderboard.RankedRace;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@Slf4j
public class LeaderboardCache {

    @Getter
    @Value("${leaderboard_cache_size}")
    private int capacity;

    private final ConcurrentSkipListMap<Integer, RankedRace> cache =
            new ConcurrentSkipListMap<>(Comparator.reverseOrder());
    private final AtomicBoolean updating = new AtomicBoolean(false);

    public RankedRace get(int rank) {
        return cache.get(rank);
    }

    public List<RankedRace> getAll() {
        return new ArrayList<>(cache.descendingMap().values());
    }

    public List<RankedRace> getSome(Integer quantity) {
        if (quantity == null) {
            return getAll();
        }
        List<RankedRace> races = new ArrayList<>();
        for (int i = 1; i <= quantity; i++) {
            races.add(cache.get(i));
        }
        return races;
    }

    public void update(List<RankedRace> races) {
        if (updating.compareAndSet(false, true)) {
            try {
                for (RankedRace race : races) {
                    add(race);
                }
                log.info("leaderboard's cached has been updated successfully");
            } catch (Exception e) {
                log.error("failed to update leaderboard's cache", e.getMessage());
            } finally {
                updating.set(false);
            }
        } else {
            log.info("leaderboard's cache is already updating");
        }
    }

    private void add(RankedRace race) {
        if (race.getRank() > capacity) {return;}
        cache.put(race.getRank(), race);
    }

}
