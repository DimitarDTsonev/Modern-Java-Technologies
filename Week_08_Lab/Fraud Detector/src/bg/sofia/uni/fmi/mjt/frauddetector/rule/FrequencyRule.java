package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAmount;
import java.util.List;

public class FrequencyRule implements Rule {

    private final int transactionCountThreshold;
    private final TemporalAmount timeWindow;
    private final double weight;
    private Clock clock = Clock.systemUTC();

    public FrequencyRule(int transactionCountThreshold, TemporalAmount timeWindow, double weight) {
        if (transactionCountThreshold <= 0) {
            throw new IllegalArgumentException("Transaction count threshold must be positive.");
        }
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be in the range [0.0, 1.0]");
        }
        if (clock == null) {
            throw new IllegalArgumentException("Clock must not be null.");
        }

        this.transactionCountThreshold = transactionCountThreshold;
        this.timeWindow = timeWindow;
        this.weight = weight;
        this.clock = clock;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        LocalDateTime now = LocalDateTime.now();
        long count = transactions.stream()
                .filter(t -> t.transactionDate().isAfter(now.minus(timeWindow)))
                .count();
        return count >= transactionCountThreshold;
    }

    @Override
    public double weight() {
        return weight;
    }
}