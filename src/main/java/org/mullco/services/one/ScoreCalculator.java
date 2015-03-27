package org.mullco.services.one;

import org.mullco.models.Game;
import org.mullco.models.Team;
import org.mullco.repos.SeasonRepository;
import thirdparty.FifaPointsService;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Comparator.comparing;

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

        ArrayList<Team> sortedTeams = new ArrayList<>(teams);
        sortedTeams.sort(comparing(Team::getPoints).thenComparing(Team::getGoalDiff).reversed());

        return sortedTeams;
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
