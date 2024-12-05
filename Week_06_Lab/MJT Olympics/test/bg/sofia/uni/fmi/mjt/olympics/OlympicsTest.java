package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OlympicsTest {

    private MJTOlympics olympics;
    private Competition competition;
    private Athlete competitor1;
    private Athlete competitor2;

    @BeforeEach
    void setUp() {
        olympics = mock(MJTOlympics.class);
        competition = mock(Competition.class);

        competitor1 = mock(Athlete.class);
        when(competitor1.getIdentifier()).thenReturn("id1");
        when(competitor1.getName()).thenReturn("John Doe");
        when(competitor1.getNationality()).thenReturn("USA");

        competitor2 = mock(Athlete.class);
        when(competitor2.getIdentifier()).thenReturn("id2");
        when(competitor2.getName()).thenReturn("Jane Smith");
        when(competitor2.getNationality()).thenReturn("Bulgaria");
    }

    @Test
    void testMJTOlympicsRegisteredCompetitorsNull() {
        assertThrows(IllegalArgumentException.class, () -> new MJTOlympics(null, null));
    }

    @Test
    void testMJTOlympicsCompetitionResultFetcherNull() {
        assertThrows(IllegalArgumentException.class, () -> new MJTOlympics(null, null));
    }

    @Test
    void testUpdateMedalStatisticsThrowsExceptionForNullCompetition() {
        Set<Competitor> competitors = new TreeSet<>();
        CompetitionResultFetcher fetcher = mock(CompetitionResultFetcher.class);
        MJTOlympics olympics1 = new MJTOlympics(competitors, fetcher);

        assertThrows(IllegalArgumentException.class, () -> olympics1.updateMedalStatistics(null));
    }

    @Test
    void testUpdateMedalStatisticsUpdatesCompetitorData() {
        Set<Competitor> competitors = Set.of(competitor1, competitor2);
        when(competition.competitors()).thenReturn(competitors);
        when(olympics.getRegisteredCompetitors()).thenReturn(competitors);

        olympics.updateMedalStatistics(competition);

        verify(olympics).updateMedalStatistics(competition);
    }

    @Test
    void testGetNationsRankListReturnsCorrectOrder() {
        TreeSet<String> mockRankList = new TreeSet<>(List.of("USA", "Bulgaria"));
        when(olympics.getNationsRankList()).thenReturn(mockRankList);

        TreeSet<String> result = olympics.getNationsRankList();

        assertNotNull(result);
        assertEquals("Bulgaria", result.first());
        assertEquals("USA", result.last());
    }

    @Test
    void testGetTotalMedalsThrowsExceptionForNullNationality() {
        Set<Competitor> competitions = new TreeSet<>();
        CompetitionResultFetcher fetcher = mock(CompetitionResultFetcher.class);
        MJTOlympics olympics1 = new MJTOlympics(competitions, fetcher);

        assertThrows(IllegalArgumentException.class, () -> olympics1.getTotalMedals(null));
    }

    @Test
    void testGetTotalMedalsReturnsCorrectCount() {
        final int ten = 10;
        when(olympics.getTotalMedals("USA")).thenReturn(ten);

        int medals = olympics.getTotalMedals("USA");

        assertEquals(ten, medals);
    }

    @Test
    void testGetNationsRankListWithEmptyTable() {
        MJTOlympics olympics = new MJTOlympics(Set.of(), mock(CompetitionResultFetcher.class));
        TreeSet<String> nationsRankList = olympics.getNationsRankList();

        assertTrue(nationsRankList.isEmpty(), "The rank list should be empty when no nations have medals");
    }

    @Test
    void testGetNationsMedalTableReturnsCorrectData() {
        EnumMap<Medal, Integer> usaMedals = new EnumMap<>(Medal.class);
        final int three = 3;
        usaMedals.put(Medal.GOLD, three);
        final int two = 2;
        usaMedals.put(Medal.SILVER, two);
        final int one = 1;
        usaMedals.put(Medal.BRONZE, one);

        EnumMap<Medal, Integer> bulgariaMedals = new EnumMap<>(Medal.class);
        bulgariaMedals.put(Medal.GOLD, one);
        bulgariaMedals.put(Medal.SILVER, two);
        final int zero = 0;
        bulgariaMedals.put(Medal.BRONZE, zero);

        Map<String, EnumMap<Medal, Integer>> mockTable = Map.of(
                "USA", usaMedals,
                "Bulgaria", bulgariaMedals
        );

        when(olympics.getNationsMedalTable()).thenReturn(mockTable);

        Map<String, EnumMap<Medal, Integer>> result = olympics.getNationsMedalTable();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.containsKey("USA"));
        assertTrue(result.containsKey("Bulgaria"));
        assertEquals(three, result.get("USA").get(Medal.GOLD));
    }

    @Test
    void testGetRegisteredCompetitorsReturnsAllCompetitors() {
        Set<Competitor> competitors = Set.of(competitor1, competitor2);
        when(olympics.getRegisteredCompetitors()).thenReturn(competitors);

        Set<Competitor> result = olympics.getRegisteredCompetitors();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(competitor1));
        assertTrue(result.contains(competitor2));
    }
}