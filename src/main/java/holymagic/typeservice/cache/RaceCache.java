package holymagic.typeservice.cache;

import holymagic.typeservice.model.race.Race;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class RaceCache {

    @Value("${race_cache_size}")
    @Getter
    private int capacity;

    @Value("${race_cache_bound}")
    private int upperBound;

    private final ConcurrentSkipListMap<Long, Race> cache =
            new ConcurrentSkipListMap<>(Comparator.reverseOrder());
    private final AtomicBoolean updating = new AtomicBoolean(false);


    public Race get(Long timestamp) {
        return cache.get(timestamp);
    }

    public Race getById(String id) {
        for (Race race : cache.values()) {
            if (race.getId().equals(id)) {
                return race;
            }
        }
        return null;
    }

    public List<Race> get(int quantity) {
        return cache.descendingMap()
                .values()
                .stream()
                .limit(Math.min(quantity, cache.size()))
                .toList();
    }

    public void add(Race race) {
        if (race.getTimestamp() > cache.lastKey()) {
            if (cache.size() >= capacity) {
                cache.remove(cache.lastKey());
            }
            cache.putIfAbsent(race.getTimestamp(), race);
        }
    }

    public void update(List<Race> races) {
        if (updating.compareAndSet(false, true)) {
            try {
                for (Race race : races) {
                    add(race);
                    log.info("race cache has been updated, size is {}", cache.size());
                }
            } catch (Exception e) {
                log.error("failed to update race cache", e);
            } finally {
                updating.set(false);
            }
        } else {
            log.info("race cache is already updating");
        }
    }

    public void initialize(List<Race> races) {
        for (Race race : races) {
            cache.put(race.getTimestamp(), race);
        }
    }

    public List<Race> getAll() {
        return new ArrayList<>(cache.descendingMap().values());
    }

    public int getSize() {
        return cache.size();
    }

    public Race getLastestRace() {
        return cache.firstEntry().getValue();
    }

    public void removeOldRaces() {
        while (cache.size() > upperBound) {
            cache.remove(cache.lastKey());
        }
        log.info("Cache has been normalized");
    }

}
