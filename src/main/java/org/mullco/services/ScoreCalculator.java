package org.mullco.services;

import org.mullco.models.Game;
import org.mullco.models.Team;
import org.mullco.repos.SeasonRepository;
import thirdparty.FifaPointsService;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class ScoreCalculator {

    private final Integer winPoints;
    private final Integer tiePoints;

    public ScoreCalculator() {
        winPoints = FifaPointsService.getWinPoints();
        tiePoints = FifaPointsService.getTiePoints();
    }

    public List<Team> getStandings() {
        SeasonRepository seasonRepository = new SeasonRepository();
        List<Game> games = seasonRepository.getGames();

        Set<Team> teams = extractTeams(games);
        for (Game game : games) {
            int goalDiff = game.homeScore - game.awayScore;

            if (goalDiff > 0) {
                winner(teams, game.home, goalDiff);
                loser(teams, game.away, goalDiff);
            } else if (game.homeScore < game.awayScore) {
                winner(teams, game.away, goalDiff);
                loser(teams, game.home, goalDiff);
            } else if (game.awayScore == game.homeScore) {
                tie(teams, game.away);
                tie(teams, game.home);
            }
        }

        ArrayList<Team> list = new ArrayList<>(teams);
//        Collections.sort(list, Comparator.comparing(Team::getPoints).reversed());


        list.sort((lhs, rhs) -> {
            if (lhs.getPoints().equals(rhs.getPoints())) {
                return rhs.getGoalDiff() - lhs.getGoalDiff();
            } else {
                return rhs.getPoints().compareTo(lhs.getPoints());
            }
        });

//        return new TreeSet<Team>(new Comparator<Team>() {
//            public int compare(Team t1, Team t2) {
//                return Integer.valueOf(t1.points).compareTo(Integer.valueOf(t2.points));
//            }
//        });

        return list;
    }

    private Set<Team> extractTeams(List<Game> games) {
        return games.stream()
                .map(g -> asList(g.home, g.away))
                .flatMap(x -> x.stream())
                .collect(Collectors.toSet());
    }

    private void winner(Set<Team> teams, Team team, int goalDiff) {
        Team winner = getTeam(teams, team.id);
        winner.points += winPoints;
        winner.goalDifferential += goalDiff;
    }

    private void loser(Set<Team> teams, Team team, int goalDiff) {
        Team loser = getTeam(teams, team.id);
        loser.goalDifferential -= goalDiff;
    }

    private void tie(Set<Team> teams, Team team) {
        getTeam(teams, team.id).points += tiePoints;
    }

    private Team getTeam(Set<Team> teams, Long teamId) {
        return teams.stream().filter(t -> t.id == teamId).findFirst().get();
    }

}
