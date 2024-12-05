package bg.sofia.uni.fmi.mjt.olympics.competition;

import bg.sofia.uni.fmi.mjt.olympics.competitor.Athlete;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CompetitionTest {

    @Test
    void testConstructorWithValidArguments() {
        Competitor competitor = new Athlete("id1", "John Doe", "USA");
        Set<Competitor> competitors = Set.of(competitor);

        Competition competition = new Competition("100m Sprint", "Running", competitors);

        assertEquals("100m Sprint", competition.name());
        assertEquals("Running", competition.discipline());
        assertEquals(competitors, competition.competitors());
    }

    @Test
    void testConstructorThrowsExceptionWhenNameIsNull() {
        Competitor competitor = new Athlete("id1", "John Doe", "USA");
        Set<Competitor> competitors = Set.of(competitor);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Competition(null, "Running", competitors));

        assertEquals("Competition name cannot be null or blank", exception.getMessage());
    }

    @Test
    void testConstructorThrowsExceptionWhenDisciplineIsNull() {
        Competitor competitor = new Athlete("id1", "John Doe", "USA");
        Set<Competitor> competitors = Set.of(competitor);

        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Competition("100m Sprint", null, competitors));

        assertEquals("Competition discipline cannot be null or blank", exception.getMessage());
    }

    @Test
    void testConstructorThrowsExceptionWhenCompetitorsIsNull() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Competition("100m Sprint", "Running", null));

        assertEquals("Competition competitors cannot be null or empty", exception.getMessage());
    }

    @Test
    void testConstructorThrowsExceptionWhenCompetitorsIsEmpty() {
        IllegalArgumentException exception = Assertions.assertThrows(IllegalArgumentException.class,
                () -> new Competition("100m Sprint", "Running", Set.of()));

        assertEquals("Competition competitors cannot be null or empty", exception.getMessage());
    }

    @Test
    void testCompetitorsReturnsUnmodifiableSet() {
        Competitor competitor = new Athlete("id1", "John Doe", "USA");
        Set<Competitor> competitors = Set.of(competitor);

        Competition competition = new Competition("100m Sprint", "Running", competitors);

        Assertions.assertThrows(UnsupportedOperationException.class,
                () -> competition.competitors().add(new Athlete("id2", "Jane Smith", "Canada")));
    }

    @Test
    void testEqualsAndHashCode() {
        Competitor competitor = new Athlete("id1", "John Doe", "USA");
        Set<Competitor> competitors = Set.of(competitor);

        Competition competition1 = new Competition("100m Sprint", "Running", competitors);
        Competition competition2 = new Competition("100m Sprint", "Running",
                Set.of(new Athlete("id2", "Jane Smith", "Canada")));

        assertEquals(competition1, competition2);
        assertEquals(competition1.hashCode(), competition2.hashCode());
    }

    @Test
    void testEqualsReturnsFalseForDifferentNameOrDiscipline() {
        Competitor competitor = new Athlete("id1", "John Doe", "USA");
        Set<Competitor> competitors = Set.of(competitor);

        Competition competition1 = new Competition("100m Sprint", "Running", competitors);
        Competition competition2 = new Competition("200m Sprint", "Running", competitors);
        Competition competition3 = new Competition("100m Sprint", "Swimming", competitors);

        Assertions.assertNotEquals(competition1, competition2);
        Assertions.assertNotEquals(competition1, competition3);
    }
}
