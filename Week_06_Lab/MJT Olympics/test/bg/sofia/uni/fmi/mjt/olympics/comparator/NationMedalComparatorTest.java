package bg.sofia.uni.fmi.mjt.olympics.comparator;

import bg.sofia.uni.fmi.mjt.olympics.MJTOlympics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NationMedalComparatorTest {

    private MJTOlympics olympics;
    private NationMedalComparator comparator;
    private final int five = 5;

    @BeforeEach
    void setUp() {
        olympics = mock(MJTOlympics.class);
        comparator = new NationMedalComparator(olympics);
    }

    @Test
    void testCompareDifferentMedals() {
        final int ten = 10;
        when(olympics.getTotalMedals("Nation1")).thenReturn(five);
        when(olympics.getTotalMedals("Nation2")).thenReturn(ten);

        int result = comparator.compare("Nation1", "Nation2");

        assertTrue(result > 0, "Nation2 should have more medals than Nation1.");
    }

    @Test
    void testCompareEqualMedalsDifferentNames() {
        when(olympics.getTotalMedals("Nation1")).thenReturn(five);
        when(olympics.getTotalMedals("Nation2")).thenReturn(five);

        int result = comparator.compare("Nation1", "Nation2");

        assertTrue(result < 0, "Nations with the same medal count should be sorted alphabetically.");
    }

    @Test
    void testCompareEqualMedalsSameNames() {
        when(olympics.getTotalMedals("Nation1")).thenReturn(five);
        when(olympics.getTotalMedals("Nation1")).thenReturn(five);

        int result = comparator.compare("Nation1", "Nation1");

        assertEquals(0, result, "Nations with the same name and medals should be equal.");
    }
}
