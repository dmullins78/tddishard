package org.mullco.services.three;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mullco.models.Game;
import org.mullco.models.Team;
import org.mullco.repos.SeasonRepository;
import org.mullco.repos.two.PointsRepo;
import org.mullco.services.two.ScoreCalculatorTwo;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScoreCalculatorThreeTest {

    @Mock
    PointsRepo pointsRepo;

    @Mock
    SeasonRepository repository;

    @InjectMocks
    ScoreCalculatorTwo calculator;

    @Before
    public void setUp() throws Exception {
        Team one = new Team(1l, "1");
        Team two = new Team(2l, "2");
        Game game = new Game(one, 3, two, 0);
        when(repository.getGames()).thenReturn(asList(game));
        when(pointsRepo.getWinPoints()).thenReturn(3);
        when(pointsRepo.getTiePoints()).thenReturn(1);
    }

    @Test
    public void shouldCalculateFirstPlace() throws Exception {
        List<Team> standings = calculator.getStandings();

        verifyTeam(standings, 0, 3, 3);
    }

    @Test
    public void shouldCalculateSecondPlace() throws Exception {
        List<Team> standings = calculator.getStandings();

        verifyTeam(standings, 1, 0, -3);
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
        verifyTeam(week2Standings, 0, 3, 3);
        assertThat(week2Standings.get(1).name, is("2"));
        verifyTeam(week2Standings, 1, 3, 0);

    }

    private void verifyTeam(List<Team> standings, int index, int points, int goalDiff) {
        assertThat(standings.get(index).points, is(points));
        assertThat(standings.get(index).goalDifferential, is(goalDiff));
    }



}