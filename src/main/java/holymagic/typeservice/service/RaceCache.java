package holymagic.typeservice.service;

import holymagic.typeservice.model.race.Race;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

@Slf4j
@Service
public class RaceCache {

    @Value("${race_cache_size}")
    private int capacity;

    @Value("${race_cache_bound}")
    private int upperBound;

    private static final TreeMap<Long, Race> CACHE = new TreeMap<>(Comparator.reverseOrder());

    public Race get(Long timestamp) {
        return CACHE.get(timestamp);
    }

    public Race getById(String id) {
        return CACHE.values()
                .stream()
                .dropWhile(race -> !race.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<Race> get(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be greater than 0");
        }
        if (quantity > capacity) {
            return getAll();
        }
        return CACHE.values().stream().limit(quantity).toList();

    }

    public void add(Race race) {
        CACHE.putIfAbsent(race.getTimestamp(), race);
    }

    public void add(List<Race> races) {
        if (races.size() > capacity) {
            log.warn("An was made attempt to add more races than allowed!");
        } else {
            for (Race race : races) {
                add(race);
            }
            log.info("Added {} races to cache", races.size());
            removeExcess();
        }
    }

    public void remove(Long timestamp) {
        CACHE.remove(timestamp);
    }

    public void removeById(String id) {
        CACHE.entrySet().removeIf(entry -> entry.getValue().getId().equals(id));
    }

    public void remove(int quantity) {
        if (quantity > CACHE.size()) {
            log.warn("An attempt was made to remove more than allowed!");
        }
        else {
            for (int i = 0; i < quantity; i++) {
                CACHE.remove(CACHE.lastKey());
            }
            log.info("Removed {} races, current size is {}", quantity, CACHE.size());
        }
    }

    public List<Race> getAll() {
        return new ArrayList<>(CACHE.values());
    }

    public int getSize() {
        return CACHE.size();
    }

    public Race getLast() {
        return CACHE.firstEntry().getValue();
    }

    public Race getFirst() {
        return CACHE.lastEntry().getValue();
    }

    public void checkBound() {
        while (CACHE.size() > upperBound) {
            CACHE.remove(CACHE.lastKey());
        }
    }

    private void removeExcess() {
        while (CACHE.size() > capacity) {
            CACHE.remove(CACHE.lastKey());
        }
    }

}
