package holymagic.typeservice.cache;

import holymagic.typeservice.model.leaderboard.RankedRace;
import holymagic.typeservice.repository.LeaderboardRepository;
import jakarta.annotation.PostConstruct;
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
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
@Slf4j
public class LeaderboardCache {

    @Getter
    @Value("${leaderboard_cache_size}")
    private int capacity;

    private final ConcurrentSkipListMap<Integer, RankedRace> cache =
            new ConcurrentSkipListMap<>();
    private final AtomicBoolean updating = new AtomicBoolean(false);
    private final LeaderboardRepository leaderboardRepository;
    private final PlatformTransactionManager txManager;

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
                log.error("failed to update leaderboard's cache: {}", e.getMessage());
            } finally {
                updating.set(false);
            }
        } else {
            log.info("leaderboard's cache is already updating");
        }
    }

    @PostConstruct
    public void initialize() {
        TransactionTemplate template = new TransactionTemplate(txManager);
        List<RankedRace> races = template.execute(new TransactionCallback<List<RankedRace>>() {
            @Override
            public List<RankedRace> doInTransaction(TransactionStatus status) {
                if (leaderboardRepository.count() != 0) {
                    return leaderboardRepository.findAll();
                }
                return null;
            }
        });
        if (races != null && !races.isEmpty()) {
            for (RankedRace race : races) {
                cache.put(race.getRank(), race);
            }
            log.info("initialized lbs cache from db successfully");
        } else {
            log.warn("couldn't initialize lbs cache from db");
        }
    }

    private void add(RankedRace race) {
        if (race.getRank() > capacity) {
            return;
        }
        cache.put(race.getRank(), race);
    }

}
