package greg.exercise.comparators;

import static org.assertj.core.api.Assertions.assertThat;

import greg.exercise.entity.LeagueTableEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class GoalDiffComparatorTest {
  
  @ParameterizedTest
  @CsvSource({
      "20,12,-1",
      "12,12,0",
      "12,20,1",
      "-20,-12,1", // Goals scored are positive, so this case should not occur but good to check anyway
      "-12,-12,0",
      "-12,-20,-1",
      "20,-12,-1",
      "0,0,0",
      "-0,-0,0",
      "-12,20,1",
  })
  @DisplayName("Tests to ensure the comparator is sorting in the correct direction")
  void goal_dif_comparator_test(int goalDiffEntry1, int goalDiffEntry2, int expectedValue) {
    // Arrange
    var comparator = new GoalDiffComparator();
    var entry1 = new LeagueTableEntry("Chelsea", 10, 6, 2, 2, 20, 9, goalDiffEntry1, 20);
    var entry2 = new LeagueTableEntry("Chelsea", 10, 6, 2, 2, 20, 9, goalDiffEntry2, 20);
    
    // Act
    var actual = comparator.compare(entry1, entry2);
    
    // Assert
    assertThat(actual).isEqualTo(expectedValue);
  }
}
