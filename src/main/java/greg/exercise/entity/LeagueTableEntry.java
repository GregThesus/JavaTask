package greg.exercise.entity;

import java.util.Objects;

public class LeagueTableEntry {
    /**
     * name of this team
     */
    private final String teamName;
    /**
     * total games played
     */
    private final int played;
    /**
     * total games won
     */
    private final int won;
    /**
     * total games drawn
     */
    private final int drawn;
    /**
     * total games lost
     */
    private final int lost;
    /**
     * total goals scored by this team
     */
    private final int goalsFor;
    /**
     * total goals against this team
     */
    private final int goalsAgainst;
    /**
     * Average absolute score difference.
     */
    private final int goalDifference;
    /**
     * total points (3 for wins, and 1 for draws, 0 for loss)
     */
    private final int points;

    public LeagueTableEntry(String teamName, int played, int won, int drawn, int lost, int goalsFor, int goalsAgainst, int goalDifference, int points) {
        this.teamName = teamName;
        this.played = played;
        this.won = won;
        this.drawn = drawn;
        this.lost = lost;
        this.goalsFor = goalsFor;
        this.goalsAgainst = goalsAgainst;
        this.goalDifference = goalDifference;
        this.points = points;
    }

    public LeagueTableEntry(String teamName) {
        this.teamName = teamName;
        this.played = 0;
        this.won = 0;
        this.drawn = 0;
        this.lost = 0;
        this.goalsFor = 0;
        this.goalsAgainst = 0;
        this.goalDifference = 0;
        this.points = 0;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getPlayed() {
        return played;
    }

    public int getWon() {
        return won;
    }

    public int getDrawn() {
        return drawn;
    }

    public int getLost() {
        return lost;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public int getGoalDifference() {
        return goalDifference;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeagueTableEntry that = (LeagueTableEntry) o;
        return played == that.played && won == that.won && drawn == that.drawn && lost == that.lost && goalsFor == that.goalsFor && goalsAgainst == that.goalsAgainst && goalDifference == that.goalDifference && points == that.points && Objects.equals(teamName, that.teamName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamName, played, won, drawn, lost, goalsFor, goalsAgainst, goalDifference, points);
    }
}
