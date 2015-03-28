package org.mullco.services.four;

import com.ninja_squad.dbsetup.DbSetup;
import com.ninja_squad.dbsetup.destination.DataSourceDestination;
import com.ninja_squad.dbsetup.operation.Insert;
import com.ninja_squad.dbsetup.operation.Operation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.mullco.models.Game;
import org.mullco.models.Team;
import org.mullco.repos.DatabaseConfig;
import org.mullco.services.one.ScoreCalculator;

import javax.sql.DataSource;
import java.util.*;

import static com.ninja_squad.dbsetup.Operations.deleteAllFrom;
import static com.ninja_squad.dbsetup.Operations.insertInto;
import static com.ninja_squad.dbsetup.Operations.sequenceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class ScoreCalculatorFourPtOneTest {

    ScoreCalculatorTestBuilder builder;
    DataSourceDestination destination;
    ScoreCalculator calculator;

    @Before
    public void setUp() throws Exception {
        DataSource dataSource = new DatabaseConfig().getDataSource();
        destination = DataSourceDestination.with(dataSource);
        calculator = new ScoreCalculator();

        DbSetup dbSetup = new DbSetup(destination, deleteAllFrom("GAMES", "TEAMS"));
        dbSetup.launch();

        builder = new ScoreCalculatorTestBuilder();
    }

    @Test
    public void shouldCalculateFirstPlace() throws Exception {
        builder.home(1, "1")
                .away(2, "2")
                .game(3, 0)
                .build();

        List<Team> standings = calculator.getStandings();

        assertTeamPointsAndGoalDiff(standings.get(0), "1", 3, 3);
    }

    @Test
    public void shouldHandleTies() {
        builder.home(1, "1")
                .away(2, "2")
                .game(1, 1)
                .build();

        List<Team> standings = calculator.getStandings();

        assertTeamPointsAndGoalDiff(standings.get(1), "2", 1, 0);
    }

    @Test
    public void shouldRankByPointsAndGoalDifferential() throws Exception {
        builder.home(1, "1")
                .away(2, "2")
                .game(3, 0)
                .home(2, "2")
                .away(3, "3")
                .game(7, 0)
                .build();

        List<Team> standings = calculator.getStandings();

        assertTeamPointsAndGoalDiff(standings.get(0), "2", 3, 4);
        assertTeamPointsAndGoalDiff(standings.get(1), "1", 3, 3);
    }

    private void assertTeamPointsAndGoalDiff(Team team, String name, int points, int goalDiff) {
        assertThat(team.name, is(name));
        assertThat(team.points, is(points));
        assertThat(team.goalDifferential, is(goalDiff));
    }

    class ScoreCalculatorTestBuilder {
        private Map<Integer, Team> teams = new HashMap<>();
        private List<Game> games = new ArrayList<>();

        public ScoreCalculatorTestBuilder home(Integer id, String name) {
            return addTeam(id, name);
        }

        public ScoreCalculatorTestBuilder away(Integer id, String name) {
            return addTeam(id, name);
        }

        private ScoreCalculatorTestBuilder addTeam(Integer id, String name) {
            if (!teams.containsKey(id)) {
                teams.put(id, new Team(id, name));
            }

            return this;
        }

        public ScoreCalculatorTestBuilder game(Integer homeScore, Integer awayScore) {
            List<Team> values = new ArrayList<>(teams.values());
            Team home = values.get(values.size() - 2);
            Team away = values.get(values.size() - 1);

            Game game = new Game(home, homeScore, away, awayScore);
            games.add(game);

            return this;
        }

        public void build() {
            List<Operation> ops = new ArrayList<>();

            for (Team team : teams.values()) {
                Insert teams = insertInto("TEAMS")
                        .columns("ID", "NAME")
                        .values(team.id, team.name)
                        .build();
                ops.add(teams);
            }

            for (Game game : games) {
                Insert gameSetup = insertInto("GAMES")
                        .columns("ID", "HOME_TEAM", "HOME_TEAM_SCORE", "AWAY_TEAM", "AWAY_TEAM_SCORE")
                        .values(new Random().nextInt(), game.home.id, game.homeScore, game.away.id, game.awayScore)
                        .build();
                ops.add(gameSetup);
            }

            DbSetup dbSetup = new DbSetup(destination, sequenceOf(ops));
            dbSetup.launch();
        }
    }

}