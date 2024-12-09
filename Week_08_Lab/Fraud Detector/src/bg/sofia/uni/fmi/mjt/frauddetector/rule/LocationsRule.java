package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

public class LocationsRule implements Rule {

    private final int threshold;
    private final double weight;

    public LocationsRule(int threshold, double weight) {
        if (weight < 0.0 || weight > 1.0) {
            throw new IllegalArgumentException("Weight must be in the range [0.0, 1.0]");
        }

        this.threshold = threshold;
        this.weight = weight;
    }

    @Override
    public boolean applicable(List<Transaction> transactions) {
        long uniqueLocations = transactions.stream()
                .map(Transaction::location)
                .distinct()
                .count();
        return uniqueLocations >= threshold;
    }

    @Override
    public double weight() {
        return weight;
    }
}