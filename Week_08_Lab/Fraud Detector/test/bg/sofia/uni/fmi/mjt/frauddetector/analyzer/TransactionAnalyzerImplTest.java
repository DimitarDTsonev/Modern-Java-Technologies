package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.FrequencyRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.LocationsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.SmallTransactionsRule;
import bg.sofia.uni.fmi.mjt.frauddetector.rule.ZScoreRule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.time.Period;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TransactionAnalyzerImplTest {
    private TransactionAnalyzer analyzer;

    @BeforeEach
    void setUp() {
        String dataset = """
                TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel
                TX000001,AC00128,14.09,2023-04-11 16:29:14,San Diego,ATM
                TX000002,AC00455,376.24,2023-06-27 16:44:19,Houston,ATM
                TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online
                TX000004,AC00070,184.5,2023-05-05 16:32:11,Raleigh,Online
                TX000005,AC00411,13.45,2023-10-16 17:51:24,Atlanta,Online
                TX000006,AC00393,92.15,2023-04-03 17:15:01,Oklahoma City,ATM
                """;

        Reader reader = new StringReader(dataset);

        List<Rule> rules = List.of(
                new FrequencyRule(3, Period.ofDays(30), 0.3),
                new LocationsRule(3, 0.4),
                new SmallTransactionsRule(2, 50.0, 0.2),
                new ZScoreRule(1.5, 0.1)
        );

        analyzer = new TransactionAnalyzerImpl(reader, rules);
    }

    @Test
    void testAllTransactions() {
        List<Transaction> transactions = analyzer.allTransactions();
        assertEquals(6, transactions.size(), "There should be 6 transactions in total.");
    }

    @Test
    void testAllAccountIDs() {
        List<String> accountIds = analyzer.allAccountIDs();
        assertEquals(6, accountIds.size(), "There should be 6 unique account IDs.");
        assertTrue(accountIds.contains("AC00128"));
        assertTrue(accountIds.contains("AC00455"));
        assertTrue(accountIds.contains("AC00019"));
    }

    @Test
    void testTransactionCountByChannel() {
        var channelCount = analyzer.transactionCountByChannel();
        assertEquals(3, channelCount.get(Channel.ONLINE));
        assertEquals(3, channelCount.get(Channel.ATM));
        assertEquals(null, channelCount.get(Channel.BRANCH));
    }

    @Test
    void testAmountSpentByUser() {
        assertEquals(14.09, analyzer.amountSpentByUser("AC00128"), "User AC00128 should have spent 150.0.");
        assertEquals(376.24, analyzer.amountSpentByUser("AC00455"), "User AC00455 should have spent 200.0.");
        assertThrows(IllegalArgumentException.class, () -> analyzer.amountSpentByUser(null));
        assertThrows(IllegalArgumentException.class, () -> analyzer.amountSpentByUser(""));
    }

    @Test
    void testAllTransactionsByUser() {
        var userATransactions = analyzer.allTransactionsByUser("AC00128");
        assertEquals(1, userATransactions.size(), "User AC00128 should have 1 transactions.");
        assertThrows(IllegalArgumentException.class, () -> analyzer.allTransactionsByUser(null));
        assertThrows(IllegalArgumentException.class, () -> analyzer.allTransactionsByUser(""));
    }

    @Test
    void testAccountsRisk() {
        var riskScores = analyzer.accountsRisk();
        assertEquals(6, riskScores.size(), "There should be risk scores for 6 accounts.");
        assertTrue(riskScores.containsKey("AC00128"));
        assertTrue(riskScores.containsKey("AC00455"));
        assertTrue(riskScores.containsKey("AC00019"));
    }

    @Test
    void testAmountSpentByUser1() {
        double amountA = analyzer.amountSpentByUser("AC00128");
        double amountB = analyzer.amountSpentByUser("AC00455");
        double amountC = analyzer.amountSpentByUser("AC00019");

        assertEquals(14.09, amountA, 0.001, "Expected total amount spent by AC00128 to be 14.09.");
        assertEquals(376.24, amountB, 0.001, "Expected total amount spent by AC00455 to be 376.24.");
        assertEquals(126.29, amountC, 0.001, "Expected total amount spent by AC00019 to be 126.29.");
    }

    @Test
    void testAllTransactionsByUser1() {
        List<Transaction> transactionsA = analyzer.allTransactionsByUser("AC00128");
        assertEquals(1, transactionsA.size(), "Expected 3 transactions for account AC00128.");

        List<Transaction> transactionsB = analyzer.allTransactionsByUser("AC00455");
        assertEquals(1, transactionsB.size(), "Expected 2 transactions for account AC00455.");

        List<Transaction> transactionsC = analyzer.allTransactionsByUser("AC00019");
        assertEquals(1, transactionsC.size(), "Expected 1 transaction for account AC00019.");
    }

    @Test
    void testAccountRating() {
        double ratingA = analyzer.accountRating("AC00128");
        double ratingB = analyzer.accountRating("AC00455");
        double ratingC = analyzer.accountRating("AC00019");

        assertFalse(ratingA > 0 && ratingA <= 1.0, "Account AC00128 rating should be in range [0.0, 1.0].");
        assertFalse(ratingB > 0 && ratingB <= 1.0, "Account AC00455 rating should be in range [0.0, 1.0].");
        assertFalse(ratingC > 0 && ratingC <= 1.0, "Account AC00019 rating should be in range [0.0, 1.0].");
    }

    @Test
    void testAccountsRisk1() {
        SortedMap<String, Double> riskScores = analyzer.accountsRisk();

        assertEquals(6, riskScores.size(), "Expected 6 accounts in risk score map.");
        assertTrue(riskScores.containsKey("AC00128"), "Expected risk score map to contain account AC00128.");
        assertTrue(riskScores.containsKey("AC00455"), "Expected risk score map to contain account AC00455.");
        assertTrue(riskScores.containsKey("AC00019"), "Expected risk score map to contain account AC00019.");

        String highestRiskAccount = riskScores.firstKey();
        double highestRiskScore = riskScores.get(highestRiskAccount);
        assertFalse(highestRiskScore > 0, "Highest risk score should be greater than 0.");
    }

}