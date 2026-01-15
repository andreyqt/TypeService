package holymagic.typeservice.cache;

import holymagic.typeservice.model.leaderboard.RankedRace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LeaderboardCacheTest {

    private LeaderboardCache leaderboardCache;
    private int testCapacity;
    private List<RankedRace> generatedRaces;

    @BeforeEach
    void setUp() {
        testCapacity = 50;
        leaderboardCache = new LeaderboardCache();
        ReflectionTestUtils.setField(leaderboardCache, "capacity", testCapacity);
        generatedRaces = provideRankedRaces(testCapacity);
        leaderboardCache.update(generatedRaces);
    }

    @Test
    public void getTest() {
        assertAll("verify that all races from the lists are in cache", () -> {
           for (int i = 0; i < testCapacity; i++) {
               assertEquals(generatedRaces.get(i), leaderboardCache.get(i+1));
           }
        });
    }

    @Test
    public void updateTest() {
        List<RankedRace> newRecords = provideNewRankedRaces(testCapacity);
        leaderboardCache.update(newRecords);
        assertAll("verify that all races are new", () -> {
            for (int i = 0; i < testCapacity; i++) {
                assertEquals(newRecords.get(i), leaderboardCache.get(i+1));
            }
        });
    }

    @Test
    public void updateWithRanks() {
        List<RankedRace> lowRankRaces = provideLowRankRaces(testCapacity, testCapacity + 1);
        leaderboardCache.update(lowRankRaces);
        assertAll("verify there are no low rank races", () -> {
            for (int i = 0; i < testCapacity; i++) {
                assertEquals(generatedRaces.get(i), leaderboardCache.get(i+1));
            }
        });
    }

    public List<RankedRace> provideRankedRaces(int quantity) {
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

    public List<RankedRace> provideNewRankedRaces(int quantity) {
        List<RankedRace> rankedRaces = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            int rank = i + 1;
            double wpm = 351. - i * 1.0;
            RankedRace rankedRace = RankedRace.builder()
                    .rank(rank)
                    .wpm(wpm)
                    .build();
            rankedRaces.add(rankedRace);
        }
        return rankedRaces;
    }

    public List<RankedRace> provideLowRankRaces(int quantity, int initRank) {
        List<RankedRace> rankedRaces = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            int rank = initRank + 1;
            double wpm = 250. - i * 1.0;
            RankedRace rankedRace = RankedRace.builder()
                    .rank(rank)
                    .wpm(wpm)
                    .build();
            rankedRaces.add(rankedRace);
        }
        return rankedRaces;
    }

}
