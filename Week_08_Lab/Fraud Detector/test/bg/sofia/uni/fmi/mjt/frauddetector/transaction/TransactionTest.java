package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionTest {

    @Test
    void testValidTransactionLine() {
        String validLine = "1,A,100.0,2023-12-01 10:15:30,Sofia,ONLINE";
        Transaction transaction = Transaction.of(validLine);

        assertEquals("1", transaction.transactionID());
        assertEquals("A", transaction.accountID());
        assertEquals(100.0, transaction.transactionAmount());
        assertEquals(LocalDateTime.of(2023, 12, 1, 10, 15, 30), transaction.transactionDate());
        assertEquals("Sofia", transaction.location());
        assertEquals(Channel.ONLINE, transaction.channel());
    }

    @Test
    void testNullLineThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.of(null),
                "Line cannot be null or empty.");
    }

    @Test
    void testEmptyLineThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.of("   "),
                "Line cannot be null or empty.");
    }

    @Test
    void testLineWithIncorrectNumberOfFieldsThrowsException() {
        String invalidLine = "1,A,100.0,2023-12-01 10:15:30";
        assertThrows(IllegalArgumentException.class, () -> Transaction.of(invalidLine),
                "Line must contain exactly 6 fields.");
    }

    @Test
    void testInvalidTransactionAmountThrowsException() {
        String invalidLine = "1,A,INVALID_AMOUNT,2023-12-01 10:15:30,Sofia,ONLINE";
        assertThrows(NumberFormatException.class, () -> Transaction.of(invalidLine));
    }

    @Test
    void testInvalidTransactionDateThrowsException() {
        String invalidLine = "1,A,100.0,INVALID_DATE,Sofia,ONLINE";
        assertThrows(Exception.class, () -> Transaction.of(invalidLine));
    }

    @Test
    void testInvalidChannelThrowsException() {
        String invalidLine = "1,A,100.0,2023-12-01 10:15:30,Sofia,INVALID_CHANNEL";
        assertThrows(IllegalArgumentException.class, () -> Transaction.of(invalidLine));
    }

    @Test
    void testTrimmedFields() {
        String validLine = "  1  , A  ,  100.0 , 2023-12-01 10:15:30 , Sofia  , online ";
        Transaction transaction = Transaction.of(validLine);

        assertEquals("1", transaction.transactionID());
        assertEquals("A", transaction.accountID());
        assertEquals(100.0, transaction.transactionAmount());
        assertEquals(LocalDateTime.of(2023, 12, 1, 10, 15, 30), transaction.transactionDate());
        assertEquals("Sofia", transaction.location());
        assertEquals(Channel.ONLINE, transaction.channel());
    }
}
