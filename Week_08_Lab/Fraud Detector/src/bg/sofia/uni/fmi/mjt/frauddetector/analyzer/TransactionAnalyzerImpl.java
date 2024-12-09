package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.BufferedReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {

    private final List<Transaction> transactions;
    private final List<Rule> rules;
    private static final double MIN = 1.0E-06;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;
    private static final int SIX = 6;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) {
        if (reader == null || rules == null || rules.isEmpty()) {
            throw new IllegalArgumentException("Reader and rules must not be null or empty.");
        }

        double totalWeight = rules.stream().mapToDouble(Rule::weight).sum();
        if (Math.abs(totalWeight - 1.0) > MIN) {
            throw new IllegalArgumentException("The sum of rule weights must equal 1.0");
        }

        this.transactions = loadTransactions(reader);
        this.rules = List.copyOf(rules);
    }

    private Transaction parseTransaction(String line) {
        String[] parts = line.split(",");
        if (parts.length != SIX) {
            throw new IllegalArgumentException("Invalid transaction format: " + line);
        }
        try {
            String id = parts[0].trim();
            String accountID = parts[1].trim();
            // За парсирането на суми, заменяме евентуалната запетая с точка
            double amount = Double.parseDouble(parts[2].trim().replace(",", "."));
            LocalDateTime date = LocalDateTime.parse(parts[THREE].trim(), DATE_FORMATTER);
            String location = parts[FOUR].trim();
            Channel channel = Channel.valueOf(parts[FIVE].trim().toUpperCase());

            return new Transaction(id, accountID, amount, date, location, channel);
        } catch (Exception e) {
            throw new RuntimeException("Error parsing transaction line: " + line, e);
        }
    }

    private List<Transaction> loadTransactions(Reader reader) {
        try (BufferedReader br = new BufferedReader(reader)) {
            return br.lines()
                    .skip(1)
                    .map(this::parseTransaction)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load transactions from reader", e);
        }
    }

    @Override
    public List<Transaction> allTransactions() {
        return List.copyOf(transactions);
    }

    @Override
    public List<String> allAccountIDs() {
        return transactions.stream()
                .map(Transaction::accountID)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        return transactions.stream()
                .collect(Collectors.groupingBy(Transaction::channel, Collectors.summingInt(t -> 1)));
    }

    @Override
    public double amountSpentByUser(String accountID) {
        if (accountID == null || accountID.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty.");
        }

        return transactions.stream()
                .filter(t -> t.accountID().equals(accountID))
                .mapToDouble(Transaction::transactionAmount)
                .sum();
    }

    @Override
    public List<Transaction> allTransactionsByUser(String accountID) {
        if (accountID == null || accountID.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty.");
        }

        return transactions.stream()
                .filter(t -> t.accountID().equals(accountID))
                .collect(Collectors.toList());
    }

    @Override
    public double accountRating(String accountID) {
        if (accountID == null || accountID.isBlank()) {
            throw new IllegalArgumentException("Account ID cannot be null or empty.");
        }

        List<Transaction> userTransactions = allTransactionsByUser(accountID);
        return rules.stream()
                .filter(rule -> rule.applicable(userTransactions))
                .mapToDouble(Rule::weight)
                .sum();
    }

    @Override
    public SortedMap<String, Double> accountsRisk() {
        Map<String, Double> riskMap = transactions.stream()
                .map(Transaction::accountID)
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        this::accountRating
                ));

        Comparator<String> comparator = (id1, id2) -> {
            int result = Double.compare(riskMap.get(id2), riskMap.get(id1));
            return result != 0 ? result : id1.compareTo(id2);
        };

        TreeMap<String, Double> sortedRiskMap = new TreeMap<>(comparator);
        sortedRiskMap.putAll(riskMap);

        return sortedRiskMap;
    }
}