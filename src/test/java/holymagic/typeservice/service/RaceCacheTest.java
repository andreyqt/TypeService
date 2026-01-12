package holymagic.typeservice.service;

import holymagic.typeservice.model.race.Race;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        raceCache.add(races);
    }

    @Test
    public void addTest() {
        assertEquals(3, raceCache.getSize());
        assertEquals(races.get(0), raceCache.getById("fi35d345"));
        assertEquals(races.get(1), raceCache.getById("298k39g0"));
        assertEquals(races.get(2), raceCache.getById("3in60nf9"));
    }

    @Test
    public void getByIdTest() {
        Race actualRace = raceCache.getById(races.getFirst().getId());
        assertEquals(races.getFirst(), actualRace);
    }

    @Test
    public void getAllTest() {
        List<Race> actualRaces = raceCache.getAll();
        List<Race> expectedRaces = races.stream()
                .sorted(Comparator.comparingLong(Race::getTimestamp).reversed())
                .toList();
        assertEquals(expectedRaces, actualRaces);
    }

    @Test
    public void getLastAndFirstTest() {
        Race actualFirst = raceCache.getFirst();
        Race actualLast = raceCache.getLast();
        Race expectedFirst = races.get(0);
        Race expectedLast = races.get(2);
        assertEquals(expectedFirst, actualFirst);
        assertEquals(expectedLast, actualLast);
    }

    @Test
    public void getTest() {
        List<Long> timestamps = races.stream().map(Race::getTimestamp).toList();
        assertNotNull(raceCache.get(timestamps.get(0)));
        assertNotNull(raceCache.get(timestamps.get(1)));
        assertNotNull(raceCache.get(timestamps.get(2)));
        assertNull(raceCache.get(15000L));
    }

    @Test
    public void removeTest() {
        assertEquals(3, raceCache.getSize());
        raceCache.remove(1767014120000L);
        assertEquals(2, raceCache.getSize());
    }

    @Test
    public void removeByIdTest() {
        assertEquals(3, raceCache.getSize());
        raceCache.removeById("3in60nf9");
        assertEquals(2, raceCache.getSize());
    }

    @Test
    public void removeMoreThanOneTest() {
        assertEquals(3, raceCache.getSize());
        raceCache.remove(2);
        assertEquals(1, raceCache.getSize());
    }

    @Test
    public void removeMoreThanSizeTest() {
        assertEquals(3, raceCache.getSize());
        raceCache.remove(4);
        assertEquals(3, raceCache.getSize());
    }

    @Test
    public void removeWithInvalidArgTest() {
        assertEquals(3, raceCache.getSize());
        Exception exception = assertThrows(IllegalArgumentException.class, () -> raceCache.get(-1));
        assertEquals("quantity must be greater than 0", exception.getMessage());
        assertEquals(3, raceCache.getSize());
    }

    @Test
    public void clearTest() {
        assertEquals(3, raceCache.getSize());
        raceCache.clear();
        assertEquals(0, raceCache.getSize());
    }

}
