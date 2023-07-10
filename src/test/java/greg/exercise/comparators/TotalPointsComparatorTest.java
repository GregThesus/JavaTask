package greg.exercise.comparators;

import static org.assertj.core.api.Assertions.assertThat;

import greg.exercise.entity.LeagueTableEntry;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TotalPointsComparatorTest {

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
  void total_points_comparator_test(int totalPointsEntry1, int totalPointsEntry2, int expectedValue) {
    // Arrange
    var comparator = new TotalPointsComparator();
    var entry1 = new LeagueTableEntry("Chelsea", 10, 6, 2, 2, 20, 9, 5, totalPointsEntry1);
    var entry2 = new LeagueTableEntry("Chelsea", 10, 6, 2, 2, 20, 9, 5, totalPointsEntry2);

    // Act
    var actual = comparator.compare(entry1, entry2);

    // Assert
    assertThat(actual).isEqualTo(expectedValue);
  }

}
