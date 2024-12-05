package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MJTOlympicsTest {

    private MJTOlympics olympics;

    @Mock
    private CompetitionResultFetcher resultFetcher;

    @Mock
    private Competitor competitor1;

    @Mock
    private Competitor competitor2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(competitor1.getNationality()).thenReturn("Bulgaria");
        when(competitor2.getNationality()).thenReturn("USA");

        Set<Competitor> competitors = Set.of(competitor1, competitor2);
        olympics = new MJTOlympics(competitors, resultFetcher);
    }

    @Test
    void testUpdateMedalStatisticsValidCompetition() {
        Competition competition = new Competition("100m Sprint", "Running", Set.of(competitor1, competitor2));
        TreeSet<Competitor> ranking = new TreeSet<>(Comparator.comparing(Competitor::getNationality));
        ranking.add(competitor1);
        ranking.add(competitor2);

        when(resultFetcher.getResult(competition)).thenReturn(ranking);

        olympics.updateMedalStatistics(competition);

        assertEquals(1, olympics.getTotalMedals("Bulgaria"));
        assertEquals(1, olympics.getTotalMedals("USA"));
    }

    @Test
    void testUpdateMedalStatisticsInvalidCompetition() {
        Competition invalidCompetition = new Competition("High Jump", "Jumping", Set.of(mock(Competitor.class)));

        assertThrows(IllegalArgumentException.class, () -> olympics.updateMedalStatistics(invalidCompetition));
    }

    @Test
    void testUpdateMedalStatisticsMoreThanThreeCompetitors() {
        Competition competition = new Competition("100m Sprint", "Running", Set.of(competitor1, competitor2));
        TreeSet<Competitor> ranking = new TreeSet<>(Comparator.comparing(Competitor::getNationality));
        Competitor competitor3 = new Athlete("id3", "Jose Rodriguez", "Brazil");
        Competitor competitor4 = new Athlete("id4", "Hans Muller", "Germany");
        Competitor competitor5 = new Athlete("id5", "James Rodriguez", "Columbia");
        ranking.add(competitor1);
        ranking.add(competitor2);
        ranking.add(competitor3);
        ranking.add(competitor4);
        ranking.add(competitor5);
        final int size = ranking.size();

        when(resultFetcher.getResult(competition)).thenReturn(ranking);

        olympics.updateMedalStatistics(competition);

        assertEquals(size, ranking.size(), "The ranking size should not be modified by the method.");
    }
}