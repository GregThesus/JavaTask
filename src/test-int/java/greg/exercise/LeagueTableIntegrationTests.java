package greg.exercise;

import greg.exercise.entity.LeagueTableEntry;
import greg.exercise.entity.Match;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

// Separated out the tests for my higher numbers to reduce time for gradle build command but still give users the ability to test very high numbers of records
public class LeagueTableIntegrationTests {

  Random random = new Random();

  @ParameterizedTest
  @CsvSource({
      "1000,10000,20",
      "10000,10000,10000",
      "999999,50,5", // Max size before heap memory issue
  })
  @DisplayName("Generate a random dataset and check the runtime for large numbers of matches and teams")
  void benchmark_tests(int numberOfTeams, int numberOfMatchesPerTeam, int maxForRandomGoalsScored) {
    // Arrange
    var teams = IntStream.range(1, numberOfTeams + 1).mapToObj(num -> String.format("Team %s", num)).toList();

    var matches = IntStream.range(0, numberOfMatchesPerTeam * numberOfTeams).mapToObj(num -> {
      var home = teams.get(random.nextInt(0, teams.size()));
      var away = getAwayTeam(home, teams);
      return new Match(home, away, random.nextInt(0, maxForRandomGoalsScored), random.nextInt(0, maxForRandomGoalsScored));
    }).toList();

    // Act
    var start = Instant.now();
    var table = new LeagueTable(matches);
    var entries = table.getTableEntries();
    var end = Instant.now();

    // Assert
    var duration = end.toEpochMilli() - start.toEpochMilli();
    System.out.println("\n==================================================");
    System.out.printf("    Time taken to execute : %s milliseconds", duration);
    System.out.println("\n==================================================\n");
//    printTable(entries); // Uncomment to print full list
  }

  // Helper Methods

  // Get a team and make sure it's not the one that's already been selected
  private String getAwayTeam(String home, List<String> teams) {
    var newTeam = teams.get(random.nextInt(0, teams.size()));
    return newTeam.equals(home) ? getAwayTeam(home, teams) : newTeam;
  }

  private void printTable(List<LeagueTableEntry> entries) {
    entries.forEach(entry -> {
      System.out.printf("\n=========== Team : %s ===========\n", entry.getTeamName());
      System.out.printf("No. Played : %s, No. Won : %s, No. Drawn : %s, No. Lost : %s, Goals For : %s, Goals Against : %s, Goal Difference : %s, Points : %s\n", entry.getPlayed(), entry.getWon(), entry.getDrawn(), entry.getLost(), entry.getGoalsFor(), entry.getGoalsAgainst(), entry.getGoalDifference(), entry.getPoints());
    });
    System.out.println("\n");
  }

}
