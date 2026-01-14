package holymagic.typeservice.service;

import holymagic.typeservice.model.leaderboard.RankedRace;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class LeaderboardCacheTest {

    private LeaderboardCache leaderboardCache;
    private int testCapacity;

    @BeforeEach
    void setUp() {
        testCapacity = 250;
        leaderboardCache = new LeaderboardCache();
        ReflectionTestUtils.setField(leaderboardCache, "capacity", testCapacity);
    }

    @AfterEach
    void tearDown() {
        leaderboardCache.clear();
    }

    public static List<RankedRace> provideRankedRaces(int quantity) {
        List<RankedRace> rankedRaces = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            int rank = i + 1;
            double wpm = 350. - i * 1.0;
            RankedRace rankedRace = RankedRace.builder()
                    .rank(rank)
                    .wpm(wpm)
                    .build();
            rankedRaces.add(rankedRace);
        }
        return rankedRaces;
    }

    public static List<RankedRace> provideLowRankRaces(int quantity) {
        List<RankedRace> rankedRaces = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            int rank = 251 + i;
            double wpm = 150. - i * 1.0;
            RankedRace rankedRace = RankedRace.builder()
                    .rank(rank)
                    .wpm(wpm)
                    .build();
            rankedRaces.add(rankedRace);
        }
        return rankedRaces;
    }

}
