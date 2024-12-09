package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.analyzer.TransactionAnalyzerImpl;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RulesTest {

    @Test
    void testFrequencyRule() {
        FrequencyRule rule = new FrequencyRule(2, Period.ofDays(1), 0.5);
        List<Transaction> transactions = List.of(
                new Transaction("1", "A", 100.0, LocalDateTime.now(), "Sofia", Channel.ONLINE),
                new Transaction("2", "A", 200.0, LocalDateTime.now(), "Plovdiv", Channel.ATM)
        );
        assertTrue(rule.applicable(transactions));
    }

    @Test
    void testLocationsRule() {
        LocationsRule rule = new LocationsRule(2, 0.3);
        List<Transaction> transactions = List.of(
                new Transaction("1", "A", 100.0, LocalDateTime.now(), "Sofia", Channel.ONLINE),
                new Transaction("2", "A", 200.0, LocalDateTime.now(), "Plovdiv", Channel.ATM)
        );
        assertTrue(rule.applicable(transactions));
    }

    @Test
    void testSmallTransactionsRule() {
        SmallTransactionsRule rule = new SmallTransactionsRule(2, 150.0, 0.2);
        List<Transaction> transactions = List.of(
                new Transaction("1", "A", 100.0, LocalDateTime.now(), "Sofia", Channel.ONLINE),
                new Transaction("2", "A", 50.0, LocalDateTime.now(), "Plovdiv", Channel.ATM)
        );
        assertTrue(rule.applicable(transactions));
    }

    @Test
    void testZScoreRule() {
        ZScoreRule rule = new ZScoreRule(1.0, 0.4);
        List<Transaction> transactions = List.of(
                new Transaction("1", "A", 100.0, LocalDateTime.now(), "Sofia", Channel.ONLINE),
                new Transaction("2", "A", 200.0, LocalDateTime.now(), "Plovdiv", Channel.ATM)
        );
        assertTrue(rule.applicable(transactions));
    }

    @Test
    void testInvalidDataThrowsException() {
        String invalidDataset = """
        TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel
        1,A,not-a-number,2023-12-01T10:15:30,Sofia,ONLINE
        2,B,200.0,not-a-date,Plovdiv,ATM
        3,,50.0,2023-12-02T14:30:00,Varna,ONLINE
        """;

        Reader reader = new StringReader(invalidDataset);
        assertThrows(RuntimeException.class, () -> new TransactionAnalyzerImpl(reader, List.of()));
    }
}