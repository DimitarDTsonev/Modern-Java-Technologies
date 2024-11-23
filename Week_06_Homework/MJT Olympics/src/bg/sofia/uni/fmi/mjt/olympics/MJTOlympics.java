package bg.sofia.uni.fmi.mjt.olympics;

import bg.sofia.uni.fmi.mjt.olympics.comparator.NationMedalComparator;
import bg.sofia.uni.fmi.mjt.olympics.competition.Competition;
import bg.sofia.uni.fmi.mjt.olympics.competition.CompetitionResultFetcher;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Competitor;
import bg.sofia.uni.fmi.mjt.olympics.competitor.Medal;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class MJTOlympics implements Olympics {

    private static final int TOP_N_COMPETITORS = 3;

    private final CompetitionResultFetcher competitionResultFetcher;
    private final Set<Competitor> registeredCompetitors;
    private final Map<String, EnumMap<Medal, Integer>> nationsMedalTable;

    public MJTOlympics(Set<Competitor> registeredCompetitors, CompetitionResultFetcher competitionResultFetcher) {
        if (registeredCompetitors == null || competitionResultFetcher == null) {
            throw new IllegalArgumentException("RegisteredCompetitors or CompetitionResultFetcher is null");
        }

        this.competitionResultFetcher = competitionResultFetcher;
        this.registeredCompetitors = registeredCompetitors;
        this.nationsMedalTable = new HashMap<>();
    }

    @Override
    public void updateMedalStatistics(Competition competition) {
        validateCompetition(competition);

        TreeSet<Competitor> ranking = competitionResultFetcher.getResult(competition);

        List<Competitor> rankedCompetitors = new ArrayList<>(ranking);

        int topN = Math.min(TOP_N_COMPETITORS, rankedCompetitors.size());
        for (int i = 0; i < topN; i++) {
            Competitor competitor = rankedCompetitors.get(i);
            Medal medal = Medal.values()[topN - i - 1];
            competitor.addMedal(medal);
            updateMedalTable(competitor, medal);
        }
    }

    private void validateCompetition(Competition competition) {
        if (competition == null) {
            throw new IllegalArgumentException("Competition cannot be null");
        }

        if (!registeredCompetitors.containsAll(competition.competitors())) {
            throw new IllegalArgumentException("Not all competitors are registered for the Olympics");
        }
    }

    private void  updateMedalTable(Competitor competitor, Medal medal) {
        nationsMedalTable.putIfAbsent(competitor.getNationality(), new EnumMap<>(Medal.class));
        EnumMap<Medal, Integer> nationMedals = nationsMedalTable.get(competitor.getNationality());
        nationMedals.put(medal, nationMedals.getOrDefault(medal, 0) + 1);
    }

    @Override
    public Set<Competitor> getRegisteredCompetitors() {
        return registeredCompetitors;
    }

    @Override
    public Map<String, EnumMap<Medal, Integer>> getNationsMedalTable() {
        return nationsMedalTable;
    }

    @Override
    public TreeSet<String> getNationsRankList() {
        TreeSet<String> nationsRankList = new TreeSet<>(new NationMedalComparator(this));
        nationsRankList.addAll(nationsMedalTable.keySet());
        return nationsRankList;
    }

    @Override
    public int getTotalMedals(String nationality) {
        validateNationality(nationality);

        EnumMap<Medal, Integer> nationMedals = nationsMedalTable.get(nationality);
        if (nationMedals == null || nationMedals.isEmpty()) {
            return 0;
        }

        int totalMedals = 0;
        for (int count : nationMedals.values()) {
            totalMedals += count;
        }

        return totalMedals;
    }

    private void validateNationality(String nationality) {
        if (nationality == null) {
            throw new IllegalArgumentException("The nationality cannot be null");
        }

        if (!nationsMedalTable.containsKey(nationality)) {
            throw new IllegalArgumentException("The nationality is not in the medal table");
        }
    }
}