package holymagic.typeservice.cache;

import holymagic.typeservice.model.leaderboard.RankedRace;
import holymagic.typeservice.repository.LeaderboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LeaderboardCacheTest {

    @InjectMocks
    private LeaderboardCache leaderboardCache;
    @Mock
    private LeaderboardRepository leaderboardRepository;
    @Mock
    private PlatformTransactionManager txManager;

    private int testCapacity;
    private List<RankedRace> generatedRaces;

    @BeforeEach
    void setUp() {
        testCapacity = 50;
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

    @Test
    @DisplayName("shows that getSome(arg) works as getAll() for null arg")
    public void getSomeForNullArgTest() {
        List<RankedRace> actualRaces = leaderboardCache.getAll();
        List<RankedRace> expectedRaces = leaderboardCache.getSome(null);
        assertEquals(expectedRaces, actualRaces);
    }

    @Test
    @DisplayName("shows that getSome(arg) retrieves first n races from cache")
    public void getSomeTest() {
        List<RankedRace> expectedSubList = generatedRaces.subList(0, 3);
        List<RankedRace> actualSubList = leaderboardCache.getSome(3);
        assertEquals(expectedSubList, actualSubList);
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
