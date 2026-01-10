package holymagic.typeservice.service;

import holymagic.typeservice.model.leaderboard.RankedRace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test
    public void addTest() {
        List<RankedRace> races = provideRankedRaces(250);
        leaderboardCache.add(races);
        int actualSize = leaderboardCache.getSize();
        assertEquals(250, actualSize);
        for (int i = 0; i < actualSize; i++) {
            assertEquals(races.get(i), leaderboardCache.getRace(i+1));
        }
    }

    @Test
    public void addMoreThanAllowedTest() {
        List<RankedRace> races = provideRankedRaces(300);
        String expectedMsg = String.format("can't add more than %d races", testCapacity);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            leaderboardCache.add(races);
        });
        assertEquals(expectedMsg, exception.getMessage());
    }

    @Test
    public void addLowRanksTest() {
        List<RankedRace> races = provideRankedRaces(100);
        List<RankedRace> racesWithLowRanks = provideLowRanksRaces(100);
        leaderboardCache.add(races);
        leaderboardCache.add(racesWithLowRanks);
        int actualSize = leaderboardCache.getSize();
        assertEquals(100, actualSize);
    }

    @Test
    public void clearTest() {
        List<RankedRace> races = provideRankedRaces(100);
        leaderboardCache.add(races);
        assertEquals(100, leaderboardCache.getSize());
        leaderboardCache.clear();
        assertEquals(0, leaderboardCache.getSize());
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

    public static List<RankedRace> provideLowRanksRaces(int quantity) {
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
