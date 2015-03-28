package org.mullco.services.four;

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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScoreCalculatorFourTest {

    ScoreCalculatorTestBuilder builder;

    @Mock
    PointsRepo pointsRepo;

    @Mock
    SeasonRepository repository;

    @InjectMocks
    ScoreCalculatorTwo calculator;

    @Before
    public void setUp() throws Exception {
        when(pointsRepo.getWinPoints()).thenReturn(3);
        when(pointsRepo.getTiePoints()).thenReturn(1);

        builder = new ScoreCalculatorTestBuilder();
    }

    @Test
    public void shouldCalculateFirstPlace() throws Exception {
        builder.home(1, "1")
                .away(2, "2")
                .game(3, 0).build();

        List<Team> standings = calculator.getStandings();

        assertTeamPointsAndGoalDiff(standings.get(0), "1", 3, 3);
    }

    @Test
    public void shouldHandleTies() {
        builder.home(1, "1")
                .away(2, "2")
                .game(1, 1).build();

        List<Team> standings = calculator.getStandings();

        assertTeamPointsAndGoalDiff(standings.get(1), "1", 1, 0);
    }

    @Test
    public void shouldRankByPointsAndGoalDifferential() throws Exception {
        builder.home(1, "1")
                .away(2, "2")
                .game(3, 0)
                .home(2, "2")
                .away(3, "3")
                .game(3, 0).build();

        List<Team> standings = calculator.getStandings();

        assertTeamPointsAndGoalDiff(standings.get(0), "1", 3, 3);
        assertTeamPointsAndGoalDiff(standings.get(1), "2", 3, 0);
    }

    private void assertTeamPointsAndGoalDiff(Team team, String name, int points, int goalDiff) {
        assertThat(team.name, is(name));
        assertThat(team.points, is(points));
        assertThat(team.goalDifferential, is(goalDiff));
    }

    class ScoreCalculatorTestBuilder {

        private Team home;
        private Team away;
        private List<Game> games;

        public ScoreCalculatorTestBuilder() {
            games = new ArrayList<>();
        }

        public ScoreCalculatorTestBuilder home(Integer id, String name) {
            home = new Team(id, name);
            return this;
        }

        public ScoreCalculatorTestBuilder away(Integer id, String name) {
            away = new Team(id, name);
            return this;
        }

        public ScoreCalculatorTestBuilder game(Integer homeScore, Integer awayScore) {
            Game game = new Game(home, homeScore, away, awayScore);
            games.add(game);
            return this;
        }

        public void build() {
            when(repository.getGames()).thenReturn(games);
        }

    }

}