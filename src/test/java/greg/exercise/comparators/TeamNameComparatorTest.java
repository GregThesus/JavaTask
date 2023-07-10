package greg.exercise.comparators;

import static org.assertj.core.api.Assertions.assertThat;

import greg.exercise.entity.LeagueTableEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TeamNameComparatorTest {

  @ParameterizedTest
  @CsvSource({
      "Arsenal,Shrewsbury,-18",
      "Shrewsbury,Arsenal,18",
      "Shrewsbury,Shrewsbury,0",
      "ManU,ManCity,18",
      "ManCity,ManU,-18",
  })
  @DisplayName("Tests to ensure the comparator is sorting in the correct direction")
  void team_name_comparator_test(String teamNameEntry1, String teamNameEntry2, int expectedValue) {
    // Arrange
    var comparator = new TeamNameComparator();
    var entry1 = new LeagueTableEntry(teamNameEntry1, 10, 6, 2, 2, 20, 9, 5, 50);
    var entry2 = new LeagueTableEntry(teamNameEntry2, 10, 6, 2, 2, 20, 9, 5, 50);

    // Act
    var actual = comparator.compare(entry1, entry2);

    // Assert
    assertThat(actual).isEqualTo(expectedValue);
  }
  
}
