package org.mullco.models;

public class Team {
    public Long id;
    public String name;
    public int points;
    public int goalDifferential;

    public Team(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getPoints() {
        return points;
    }

    public Integer getGoalDiff() {
        return goalDifferential;
    }

}
