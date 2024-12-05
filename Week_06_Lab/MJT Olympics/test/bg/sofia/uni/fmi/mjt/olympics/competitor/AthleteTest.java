package bg.sofia.uni.fmi.mjt.olympics.competitor;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AthleteTest {
    private final int three = 3;

    @Test
    void athleteTestNullIdentifier() {
        assertThrows(IllegalArgumentException.class, () -> new Athlete(null, "Josh", "USA"));
    }

    @Test
    void athleteTestNullName() {
        assertThrows(IllegalArgumentException.class, () -> new Athlete("id1", null, "USA"));
    }

    @Test
    void athleteTestNullNationality() {
        assertThrows(IllegalArgumentException.class, () -> new Athlete("id1", "Josh", null));
    }

    @Test
    void testGetIdentifierReturnsCorrectValue() {
        Athlete athlete = new Athlete("123", "John Doe", "USA");

        assertEquals("123", athlete.getIdentifier(), "getIdentifier() should return the correct identifier");
    }

    @Test
    void testGetNameReturnsCorrectValue() {
        Athlete athlete = new Athlete("123", "John Doe", "USA");

        assertEquals("John Doe", athlete.getName(), "getName() should return the correct name");
    }

    @Test
    void addMedalThrowsIllegalArgumentExceptionForNullMedal() {
        Athlete athlete = new Athlete("id1", "Athlete1", "Bulgarian");

        assertThrows(IllegalArgumentException.class, () -> athlete.addMedal(null));
    }

    @Test
    void testGetMedalsEmptyCollectionInitially() {
        Athlete athlete = new Athlete("ID1", "John Doe", "NationA");
        assertTrue(athlete.getMedals().isEmpty());
    }

    @Test
    void testAddSingleMedal() {
        Athlete athlete = new Athlete("123", "John Doe", "USA");
        athlete.addMedal(Medal.GOLD);

        assertEquals(1, athlete.getMedals().size(), "Athlete should have one medal");
        assertTrue(athlete.getMedals().contains(Medal.GOLD), "Athlete should have a GOLD medal");
    }

    @Test
    void testNumberOfMedalsOfParticularTypeGold() {
        Athlete athlete = new Athlete("123", "John Doe", "USA");
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.GOLD);

        List<Medal> expectedMedals = List.of(Medal.GOLD, Medal.GOLD, Medal.GOLD);
        assertEquals(three, athlete.getMedals().size(), "Athlete should have three medals");
        assertTrue(athlete.getMedals().containsAll(expectedMedals), "Athlete medals are incorrect");
    }

    @Test
    void testNumberOfMedalsOfParticularTypeSilver() {
        Athlete athlete = new Athlete("123", "John Doe", "USA");
        athlete.addMedal(Medal.SILVER);
        athlete.addMedal(Medal.SILVER);
        athlete.addMedal(Medal.SILVER);

        List<Medal> expectedMedals = List.of(Medal.SILVER, Medal.SILVER, Medal.SILVER);
        assertEquals(three, athlete.getMedals().size(), "Athlete should have three medals");
        assertTrue(athlete.getMedals().containsAll(expectedMedals), "Athlete medals are incorrect");
    }

    @Test
    void testNumberOfMedalsOfParticularTypeBronze() {
        Athlete athlete = new Athlete("123", "John Doe", "USA");
        athlete.addMedal(Medal.BRONZE);
        athlete.addMedal(Medal.BRONZE);
        athlete.addMedal(Medal.BRONZE);

        List<Medal> expectedMedals = List.of(Medal.BRONZE, Medal.BRONZE, Medal.BRONZE);
        assertEquals(three, athlete.getMedals().size(), "Athlete should have three medals");
        assertTrue(athlete.getMedals().containsAll(expectedMedals), "Athlete medals are incorrect");
    }

    @Test
    void testAddMultipleMedals() {
        Athlete athlete = new Athlete("123", "John Doe", "USA");
        athlete.addMedal(Medal.GOLD);
        athlete.addMedal(Medal.SILVER);
        athlete.addMedal(Medal.BRONZE);

        List<Medal> expectedMedals = List.of(Medal.GOLD, Medal.SILVER, Medal.BRONZE);
        assertEquals(three, athlete.getMedals().size(), "Athlete should have three medals");
        assertTrue(athlete.getMedals().containsAll(expectedMedals), "Athlete medals are incorrect");
    }

    @Test
    void testEqualsWhenSameObject() {
        Athlete athlete = new Athlete("123", "John Doe", "USA");
        Athlete athlete1 = new Athlete("123", "John Doe", "USA");
        assertEquals(athlete, athlete1, "equals should return true when comparing the same object");
    }

    @Test
    void testEqualsWhenObjectsAreEqual() {
        Athlete athlete1 = new Athlete("123", "John Doe", "USA");
        Athlete athlete2 = new Athlete("123", "John Doe", "USA");

        athlete1.addMedal(Medal.GOLD);
        athlete2.addMedal(Medal.GOLD);

        assertEquals(athlete1, athlete2, "equals should return true for athletes with the same name, " +
                "nationality, and medals");
    }

    @Test
    void testEqualsWhenObjectsAreNotEqual() {
        Athlete athlete1 = new Athlete("123", "John Doe", "USA");
        Athlete athlete2 = new Athlete("456", "Jane Doe", "Canada");

        assertNotEquals(athlete1, athlete2, "equals should return false for athletes with different attributes");
    }

    @Test
    void testEqualsWhenComparedWithNull() {
        Athlete athlete = new Athlete("123", "John Doe", "USA");
        assertNotEquals(null, athlete, "equals should return false when compared with null");
    }

    @Test
    void testEqualsWhenComparedWithDifferentClass() {
        Athlete athlete = new Athlete("123", "John Doe", "USA");
        String differentObject = "Not an Athlete";

        assertNotEquals(athlete, differentObject,
                "equals should return false when compared with an object of a different class");
    }
}