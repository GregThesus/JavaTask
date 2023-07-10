package greg.exercise;

import static org.assertj.core.api.Assertions.assertThat;

import greg.exercise.entity.LeagueTableEntry;
import greg.exercise.entity.Match;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class LeagueTableTests {

  Random random = new Random();

  @Test
  @DisplayName("Sample test")
  void sample_test() {
    // Arrange

    var matches = new ArrayList<Match>() {{
        add(new Match("Chelsea", "West Ham", 2, 0));
        add(new Match("West Ham", "Shrewsbury", 2, 0));
        add(new Match("Shrewsbury", "Chelsea", 2, 0));
        add(new Match("Chelsea", "West Ham", 2, 0));
        add(new Match("West Ham", "Shrewsbury", 2, 2));
        add(new Match("Shrewsbury", "Chelsea", 2, 5));
        add(new Match("Chelsea", "West Ham", 2, 0));
        add(new Match("West Ham", "Shrewsbury", 2, 0));
        add(new Match("Shrewsbury", "Chelsea", 0, 5));
      }};

    // Act

    var table = new LeagueTable(matches);

    // Assert
    var expectedTable = List.of(
        new LeagueTableEntry("CHELSEA", 6, 5, 0, 1, 16, 4, 3, 15),
        new LeagueTableEntry("WEST HAM", 6, 2, 1, 3, 6, 8, 2, 7),
        new LeagueTableEntry("SHREWSBURY", 6, 1, 1, 4, 6, 16, 2, 4)
    );
    assertThat(table.getTableEntries()).containsExactlyElementsOf(expectedTable);
  }
  
  @Test
  @DisplayName("Test sorting rules using reflections")
  @SuppressWarnings("unchecked")
  void sorting_rules_test() throws NoSuchFieldException, IllegalAccessException {
    // Arrange
    var privateStaticField = LeagueTable.class.getDeclaredField("SORTING_RULES");
    privateStaticField.setAccessible(true);
    var comparator = (Comparator<LeagueTableEntry>) privateStaticField.get(null);

    var expected = List.of(
        new LeagueTableEntry("ABERCROMBE", 6, 5, 0, 1, 16, 4, 10, 15),
        new LeagueTableEntry("ZIMBABWE", 6, 5, 0, 1, 16, 4, 10, 15),
        new LeagueTableEntry("ABERCROMBE3", 6, 5, 0, 1, 10, 4, 10, 15),
        new LeagueTableEntry("ZIMBABWE4", 6, 5, 0, 1, 10, 4, 10, 15),
        new LeagueTableEntry("ABERCROMBE5", 6, 5, 0, 1, 16, 4, 5, 15),
        new LeagueTableEntry("ZIMBABWE6", 6, 5, 0, 1, 16, 4, 5, 15),
        new LeagueTableEntry("ABERCROMBE7", 6, 5, 0, 1, 10, 4, 5, 15),
        new LeagueTableEntry("ZIMBABWE8", 6, 5, 0, 1, 10, 4, 5, 15),
        new LeagueTableEntry("ABERCROMBE9", 6, 5, 0, 1, 16, 4, 10, 14),
        new LeagueTableEntry("ZIMBABWE10", 6, 5, 0, 1, 16, 4, 10, 14),
        new LeagueTableEntry("ABERCROMBE11", 6, 5, 0, 1, 10, 4, 10, 14),
        new LeagueTableEntry("ZIMBABWE12", 6, 5, 0, 1, 10, 4, 10, 14),
        new LeagueTableEntry("ABERCROMBE13", 6, 5, 0, 1, 16, 4, 5, 14),
        new LeagueTableEntry("ZIMBABWE14", 6, 5, 0, 1, 16, 4, 5, 14),
        new LeagueTableEntry("ABERCROMBE15", 6, 5, 0, 1, 10, 4, 5, 14),
        new LeagueTableEntry("ZIMBABWE16", 6, 5, 0, 1, 10, 4, 5, 14)
    );
    var shuffledList = new ArrayList<>(expected);

    Collections.shuffle(shuffledList);
    assertThat(shuffledList).doesNotContainSequence(expected);
    
    // Act
    shuffledList.sort(comparator);
    
    // Assert
    assertThat(shuffledList).containsExactlyElementsOf(expected);
  }

  @ParameterizedTest
  @CsvSource({
      "25,48,5",
      "50,10,4",
      "30,100,5",
  })
  @DisplayName("Tests with a random dataset")
  void random_dataset(int numberOfTeams, int numberOfMatchesPerTeam, int maxForRandomGoalsScored) {
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
    assertThat(entries).isNotEmpty();
    var duration = end.toEpochMilli() - start.toEpochMilli();
    assertThat(duration).isLessThan(8000);
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
