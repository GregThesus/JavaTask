package greg.exercise.comparators;

import greg.exercise.entity.LeagueTableEntry;
import java.util.Comparator;

public class GoalDiffComparator implements Comparator<LeagueTableEntry> {
  
  @Override
  public int compare(LeagueTableEntry o1, LeagueTableEntry o2) {
    // Negative for descending order
    return -Integer.compare(o1.getGoalDifference(), o2.getGoalDifference());
  }
}
