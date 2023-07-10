package greg.exercise.comparators;

import greg.exercise.entity.LeagueTableEntry;
import java.util.Comparator;

public class GoalsScoredComparator implements Comparator<LeagueTableEntry> {
  
  @Override
  public int compare(LeagueTableEntry o1, LeagueTableEntry o2) {
    // Negative for descending order
    return -Integer.compare(o1.getGoalsFor(), o2.getGoalsFor());
  }
}
