package greg.exercise;

import greg.exercise.comparators.GoalDiffComparator;
import greg.exercise.comparators.GoalsScoredComparator;
import greg.exercise.comparators.TeamNameComparator;
import greg.exercise.comparators.TotalPointsComparator;
import greg.exercise.entity.LeagueTableEntry;
import greg.exercise.entity.Match;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class LeagueTable {

  // static comparator as currently the rules won't change, however, in future could add functionality to provide rules as part of the constructor
  // so different LeagueTables could have different sorting.
  private static final Comparator<LeagueTableEntry> SORTING_RULES = new TotalPointsComparator() // Sort by total points (descending)
      .thenComparing(new GoalDiffComparator())    // Then by goal difference (descending)
      .thenComparing(new GoalsScoredComparator()) // Then by goals scored (descending)
      .thenComparing(new TeamNameComparator());   // Then by team name (in alphabetical order)

  // Static declarations should come before non-static
  private final List<LeagueTableEntry> leagueTableEntries; 

  /**
   * Instantiates a new League table by transforming the given List of @Match into @LeagueTableEntry and sorting the entries based on the sorting rules above
   *
   * @param matches the matches to be transformed and sorted
   */
  public LeagueTable(final List<Match> matches) {
    this.leagueTableEntries = transformMatchesToTableEntries(matches).sorted(SORTING_RULES).toList(); // Assign unsorted entries
  }

  public List<LeagueTableEntry> getTableEntries() {
    return leagueTableEntries;
  }

  // Example future usage of class
//  public void addMatches(List<Match> matches) {
//    var newTableEntries = transformMatchesToTableEntries(matches);
//
//    this.leagueTableEntries = Stream.concat(leagueTableEntries.stream(), newTableEntries).sorted(SORTING_RULES).toList();
//  }

  // ======================================================================================================================================================
  //                                                      HELPER METHODS:
  // The helper methods are separated out into multiple methods to both improve code readability and remove god methods that contain all functionality
  // ======================================================================================================================================================

  private static Stream<LeagueTableEntry> transformMatchesToTableEntries(List<Match> matches) {
    // Having a temporary table allows for new lists of matches to be added without affecting the main table. Essentially makes it safe to use outside the constructor
    var tempLeagueEntries = new HashMap<String, LeagueTableEntry>();
    var teamGoalDiffs = new HashMap<String, List<Integer>>();

    matches.forEach(match -> updateEntries(tempLeagueEntries, teamGoalDiffs, match));

    // Calculate and fill average absolute goal difference
    return tempLeagueEntries.values().stream().map(entry -> calculateAndFillGoalDiff(teamGoalDiffs, entry));
  }

  private static void updateEntries(Map<String, LeagueTableEntry> entryMap, Map<String, List<Integer>> teamGoalDiffs, Match match) {
    var homeTeam = match.getHomeTeam().toUpperCase();
    var awayTeam = match.getAwayTeam().toUpperCase();

    // Update absolute goal difference for each team
    addGoalDiff(teamGoalDiffs, homeTeam, match.getHomeScore(), awayTeam, match.getAwayScore());

    // Update home Team
    upsertTableEntry(entryMap, homeTeam, match.getHomeScore(), match.getAwayScore());
    // Update away Team
    upsertTableEntry(entryMap, awayTeam, match.getAwayScore(), match.getHomeScore());
  }

  private static void addGoalDiff(Map<String, List<Integer>> goalDiffs, String homeTeam, int homeScore, String awayTeam, int awayScore) {
    var difference = Math.abs(homeScore - awayScore); // calculate goal diff for this match

    // Get existing goalDiffs or creates new entry if one didn't already exist
    var homeGoalDiffs = goalDiffs.computeIfAbsent(homeTeam, k -> new ArrayList<>());
    var awayGoalDiffs = goalDiffs.computeIfAbsent(awayTeam, k -> new ArrayList<>());

    // Since the difference is absolute, it will be the same value for both teams
    homeGoalDiffs.add(difference);
    awayGoalDiffs.add(difference);
  }

  private static void upsertTableEntry(Map<String, LeagueTableEntry> leagueTableEntries, String teamName, int goalsFor, int goalsAgainst) {
    var matchPoints = calculatePointsForMatch(goalsFor, goalsAgainst);

    // Adds empty league entry if the team does not already exist
    var currentRecord = leagueTableEntries.computeIfAbsent(teamName, LeagueTableEntry::new);

    // Values on new lines to improve code readability
    leagueTableEntries.put(teamName, new LeagueTableEntry(
        teamName,
        currentRecord.getPlayed() + 1,
        currentRecord.getWon() + (goalsFor > goalsAgainst ? 1 : 0),
        currentRecord.getDrawn() + (goalsFor == goalsAgainst ? 1 : 0),
        currentRecord.getLost() + (goalsFor < goalsAgainst ? 1 : 0),
        currentRecord.getGoalsFor() + goalsFor,
        currentRecord.getGoalsAgainst() + goalsAgainst,
        -1, // Dummy value set as the type must be int, but we want to calculate average so rounding errors would occur if we just used int.
        currentRecord.getPoints() + matchPoints));
  }

  private static int calculatePointsForMatch(int forGoals, int againstGoals) {
    // Checks if team won the match, then if the team drew the match and returns a loss by default
    return (forGoals > againstGoals) ? 3 : (forGoals == againstGoals) ? 1 : 0;
  }

  private static LeagueTableEntry calculateAndFillGoalDiff(Map<String, List<Integer>> teamGoalDiffs, LeagueTableEntry entry) {
    var goalDiffs = teamGoalDiffs.get(entry.getTeamName());
    var averageGoalDiff = goalDiffs.stream().mapToInt(Integer::intValue).average().orElse(-1); // Double format

    // No setter/toBuilder method so creating new Entry each time. Code on new line to keep line length to a minimum for readability
    return new LeagueTableEntry(entry.getTeamName(), entry.getPlayed(), entry.getWon(), entry.getDrawn(), entry.getLost(), entry.getGoalsFor(),
        entry.getGoalsAgainst(), (int) Math.round(averageGoalDiff), entry.getPoints());
  }
}
