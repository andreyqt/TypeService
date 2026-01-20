package holymagic.typeservice.cache;

import holymagic.typeservice.model.race.Race;
import holymagic.typeservice.repository.RaceRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class RaceCache {

    @Value("${race_cache_size}")
    @Getter
    private int capacity;

    @Value("${race_cache_bound}")
    private int upperBound;

    private final ConcurrentSkipListMap<Long, Race> cache =
            new ConcurrentSkipListMap<>(Comparator.reverseOrder());
    private final AtomicBoolean updating = new AtomicBoolean(false);
    private final PlatformTransactionManager txManager;
    private final RaceRepository raceRepository;

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

    public List<Race> get(Integer quantity) {
        if (quantity == null) {
            return getAll();
        }
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
                }
                log.info("race cache has been updated, size is {}", cache.size());
            } catch (Exception e) {
                log.error("failed to update race cache", e);
            } finally {
                updating.set(false);
            }
        } else {
            log.info("race cache is already updating");
        }
    }

    public List<Race> getAfter(Long onOrAfterTimestamp) {
        if (onOrAfterTimestamp == null) {
            return getAll();
        }
        List<Race> races = new ArrayList<>();
        for (Map.Entry<Long, Race> entry : cache.entrySet()) {
            if (entry.getKey() >= onOrAfterTimestamp) {
                races.add(entry.getValue());
            }
        }
        return races;
    }

    public void initialize(List<Race> races) {
        for (Race race : races) {
            cache.put(race.getTimestamp(), race);
        }
    }

    public void initializeFromDb() {
        try {
            TransactionTemplate txTemplate = new TransactionTemplate(txManager);
            List<Race> racesFromDb = txTemplate.execute(new TransactionCallback<List<Race>>() {
                @Override
                public List<Race> doInTransaction(TransactionStatus status) {
                    return raceRepository.findForCache(capacity);
                }
            });
            if (!racesFromDb.isEmpty()) {
                initialize(racesFromDb);
                log.info("initialized race cache from db successfully");
            } else {
                log.warn("couldn't initialize race cache from db: db is empty");
            }
        } catch (Exception e) {
            log.error("failed initialize race cache from db: {}", e.getMessage());
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
