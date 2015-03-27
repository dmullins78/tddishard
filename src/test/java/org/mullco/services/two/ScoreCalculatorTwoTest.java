package org.mullco.services.two;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mullco.models.Game;
import org.mullco.models.Team;
import org.mullco.repos.SeasonRepository;
import org.mullco.repos.two.PointsRepo;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScoreCalculatorTwoTest {

    @Mock
    PointsRepo pointsRepo;

    @Mock
    SeasonRepository repository;

    @InjectMocks
    ScoreCalculatorTwo calculator;

    @Test
    public void shouldRankTeams() throws Exception {
        Team one = new Team(1l, "1");
        Team two = new Team(2l, "2");
        Game game = new Game(one, 3, two, 0);
        when(repository.getGames()).thenReturn(asList(game));
        when(pointsRepo.getWinPoints()).thenReturn(3);
        when(pointsRepo.getTiePoints()).thenReturn(1);

        List<Team> standings = calculator.getStandings();

        assertThat(standings.get(0).points, is(3));
        assertThat(standings.get(0).goalDifferential, is(3));
        assertThat(standings.get(1).points, is(0));
        assertThat(standings.get(1).goalDifferential, is(-3));

        one = new Team(1l, "1");
        two = new Team(2l, "2");
        game = new Game(one, 3, two, 0);

        Team three = new Team(3l, "3");
        Game game2 = new Game(two, 3, three, 0);
        when(repository.getGames()).thenReturn(asList(game, game2));

        List<Team> week2Standings = calculator.getStandings();

        assertThat(week2Standings.get(0).name, is("1"));
        assertThat(week2Standings.get(0).points, is(3));
        assertThat(week2Standings.get(0).goalDifferential, is(3));
        assertThat(week2Standings.get(1).name, is("2"));
        assertThat(week2Standings.get(1).points, is(3));
        assertThat(week2Standings.get(1).goalDifferential, is(0));
    }

    @Test
    public void shouldRankByPoints() throws Exception {
        Team one = new Team(1l, "1");
        Team two = new Team(2l, "2");
        Game game = new Game(one, 3, two, 0);
        when(repository.getGames()).thenReturn(asList(game));
        when(pointsRepo.getWinPoints()).thenReturn(3);
        when(pointsRepo.getTiePoints()).thenReturn(1);

        List<Team> standings = calculator.getStandings();

        assertThat(standings.get(0).points, is(3));
        assertThat(standings.get(0).goalDifferential, is(3));
        assertThat(standings.get(1).points, is(0));
        assertThat(standings.get(1).goalDifferential, is(-3));
    }

    @Test
    public void shouldRankByPointsAndGoalDifferential() throws Exception {
        Team one = new Team(1l, "1");
        Team two = new Team(2l, "2");
        Game game = new Game(one, 3, two, 0);

        Team three = new Team(3l, "3");
        Game game2 = new Game(two, 3, three, 0);
        when(repository.getGames()).thenReturn(asList(game, game2));
        when(pointsRepo.getWinPoints()).thenReturn(3);
        when(pointsRepo.getTiePoints()).thenReturn(1);

        List<Team> week2Standings = calculator.getStandings();

        assertThat(week2Standings.get(0).name, is("1"));
        assertThat(week2Standings.get(0).points, is(3));
        assertThat(week2Standings.get(0).goalDifferential, is(3));
        assertThat(week2Standings.get(1).name, is("2"));
        assertThat(week2Standings.get(1).points, is(3));
        assertThat(week2Standings.get(1).goalDifferential, is(0));

    }



}