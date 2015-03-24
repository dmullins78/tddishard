package org.mullco.services;

import org.mullco.models.Game;
import org.mullco.models.Team;
import org.mullco.repos.SeasonRepository;
import thirdparty.FifaPointsService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScoreCalculator {

    public List<Team> getStandings(Long seasonId) {
        SeasonRepository seasonRepository = new SeasonRepository();

        Integer pointsForWin = FifaPointsService.getWinPoints();

        List<Game> games = seasonRepository.getGamesBySeason(seasonId);
        Set<Team> teams = new HashSet<>();
        teams.addAll(homeTeams(games));
        teams.addAll(awayTeams(games));

        for (Game game : games) {
            if (game.awayScore > game.homeScore) {

            }
        }

        return null;
    }

    private Set<Team> awayTeams(List<Game> games) {
        return games.stream()
                .map(g -> g.away)
                .collect(Collectors.toSet());
    }

    private Set<Team> homeTeams(List<Game> games) {
        return games.stream()
                .map(g -> g.home)
                .collect(Collectors.toSet());
    }

}
