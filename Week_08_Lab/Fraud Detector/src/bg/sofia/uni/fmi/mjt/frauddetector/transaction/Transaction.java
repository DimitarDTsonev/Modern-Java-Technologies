package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(String transactionID, String accountID, double transactionAmount,
                          LocalDateTime transactionDate, String location, Channel channel) {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;

    public static Transaction of(String line) {
        if (line == null || line.isBlank()) {
            throw new IllegalArgumentException("Line cannot be null or empty.");
        }

        String[] parts = line.split(",");
        if (parts.length != SIX) {
            throw new IllegalArgumentException("Line must contain exactly 6 fields.");
        }

        String transactionID = parts[0].trim();
        String accountID = parts[1].trim();
        double transactionAmount = Double.parseDouble(parts[2].trim());
        LocalDateTime transactionDate = LocalDateTime.parse(parts[THREE].trim(), DATE_FORMATTER);
        String location = parts[FOUR].trim();
        Channel channel = Channel.valueOf(parts[FIVE].trim().toUpperCase());

        return new Transaction(transactionID, accountID, transactionAmount, transactionDate, location, channel);
    }
}