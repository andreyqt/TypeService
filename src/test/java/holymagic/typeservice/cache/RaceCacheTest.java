package holymagic.typeservice.cache;

import holymagic.typeservice.model.race.Race;
import holymagic.typeservice.service.RaceServiceTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;

import static holymagic.typeservice.service.RaceServiceTestData.NEW_RACE;
import static holymagic.typeservice.service.RaceServiceTestData.OUTDATED_RACE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class RaceCacheTest {

    private RaceCache raceCache;
    private List<Race> races;

    @BeforeEach
    void setUp() {
        raceCache = new RaceCache();
        ReflectionTestUtils.setField(raceCache, "capacity", 5);
        ReflectionTestUtils.setField(raceCache, "upperBound", 3);
        races = RaceServiceTestData.provideRaces();
        raceCache.initialize(races);
    }

    @Test
    public void getTest() {
        Race actualRace = raceCache.get(1767014109000L);
        Race expectedRace = races.get(0);
        assertEquals(expectedRace, actualRace);
    }

    @Test
    public void getByAbsentTimestampTest() {
        Race actualRace = raceCache.get(1767014108000L);
        assertNull(actualRace);
    }

    @Test
    public void getByIdTest() {
        Race actualRace = raceCache.getById("test_id_1");
        Race expectedRace = races.get(0);
        assertEquals(expectedRace, actualRace);
    }

    @Test
    public void getByAbsentIdTest() {
        Race actualRace = raceCache.getById("invalid_id");
        assertNull(actualRace);
    }

    @Test
    public void getListTest() {
        List<Race> actualRaces = raceCache.get(3);
        List<Race> expectedRaces = races.subList(0,3);
        assertEquals(expectedRaces, actualRaces);
    }

    @Test
    public void getListWithGreaterCapacityTest() {
        List<Race> actualRaces = raceCache.get(10);
        List<Race> expectedRaces = races;
        assertEquals(expectedRaces, actualRaces);
    }

    @Test
    public void addTest() {
        assertNull(raceCache.get(1767014115000L));
        assertNotNull(raceCache.get(1767014109000L));
        assertEquals(5, raceCache.getSize());
        raceCache.add(NEW_RACE);
        assertEquals(NEW_RACE, raceCache.get(1767014115000L));
        assertEquals(5, raceCache.getSize());
        assertNull(raceCache.get(1767014109000L));
    }

    @Test
    public void addOutdatedTest() {
        assertNull(raceCache.get(1767014108000L));
        assertEquals(5, raceCache.getSize());
        raceCache.add(OUTDATED_RACE);
        assertNull(raceCache.get(1767014108000L));
        assertEquals(5, raceCache.getSize());
    }

    @Test
    public void clearTest() {
        assertEquals(5, raceCache.getSize());
        clearCache();
        assertEquals(0, raceCache.getSize());
    }

    @Test
    public void getAllTest() {
        List<Race> actualRaces = raceCache.getAll();
        List<Race> expectedRaces = races;
        assertEquals(expectedRaces, actualRaces);
    }

    @Test
    public void getSizeTest() {
        assertEquals(races.size(), raceCache.getSize());
    }

    @Test
    public void getLatestTest() {
        Race actualRace = raceCache.getLastestRace();
        Race expectedRace = races.get(raceCache.getSize() - 1);
        assertEquals(expectedRace, actualRace);
    }

    @Test
    public void removeOldRacesTest() {
        assertNotNull(raceCache.get(1767014109000L));
        assertNotNull(raceCache.get(1767014110000L));
        assertEquals(5, raceCache.getSize());
        raceCache.removeOldRaces();
        assertEquals(3, raceCache.getSize());
        assertNull(raceCache.get(1767014109000L));
        assertNull(raceCache.get(1767014110000L));
    }

    public void clearCache() {
        Map<?,?> cache = (Map<?,?>) ReflectionTestUtils.getField(raceCache, "cache");
        cache.clear();
    }

}
