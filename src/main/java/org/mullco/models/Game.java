package org.mullco.models;

public class Game {
    public Long id;
    public Team home;
    public Team away;
    public Integer homeScore;
    public Integer awayScore;

    public Game(Team home, int homeScore, Team away, int awayScore) {
        this.home = home;
        this.homeScore = homeScore;
        this.away = away;
        this.awayScore = awayScore;
    }

}
