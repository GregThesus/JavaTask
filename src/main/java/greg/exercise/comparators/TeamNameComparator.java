package greg.exercise.comparators;

import greg.exercise.entity.LeagueTableEntry;
import java.util.Comparator;

public class TeamNameComparator implements Comparator<LeagueTableEntry> {
  
  @Override
  public int compare(LeagueTableEntry o1, LeagueTableEntry o2) {
    return o1.getTeamName().compareTo(o2.getTeamName());
  }
}
